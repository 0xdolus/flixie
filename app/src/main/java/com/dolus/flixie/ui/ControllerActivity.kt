package com.dolus.flixie.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.MediaSeekOptions
import com.google.android.gms.cast.MediaStatus.REPEAT_MODE_REPEAT_OFF
import com.google.android.gms.cast.MediaTrack
import com.google.android.gms.cast.TextTrackStyle
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.cast.framework.media.uicontroller.UIController
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity
import com.dolus.flixie.R
import com.dolus.flixie.mvvm.Resource
import com.dolus.flixie.mvvm.logError
import com.dolus.flixie.mvvm.safeApiCall
import com.dolus.flixie.sortUrls
import com.dolus.flixie.ui.player.LOADTYPE_CHROMECAST
import com.dolus.flixie.ui.player.RepoLinkGenerator
import com.dolus.flixie.ui.player.SubtitleData
import com.dolus.flixie.ui.result.ResultEpisode
import com.dolus.flixie.ui.subtitles.ChromecastSubtitlesFragment
import com.dolus.flixie.utils.AppContextUtils.sortSubs
import com.dolus.flixie.utils.AppUtils.parseJson
import com.dolus.flixie.utils.AppUtils.toJson
import com.dolus.flixie.utils.CastHelper.awaitLinks
import com.dolus.flixie.utils.CastHelper.getMediaInfo
import com.dolus.flixie.utils.Coroutines.ioSafe
import com.dolus.flixie.utils.DataStoreHelper
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.Qualities
import com.dolus.flixie.utils.UIHelper.dismissSafe
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.json.JSONObject

private fun RemoteMediaClient.getItemIndex(): Int? {
    return try {
        val index = this.mediaQueue.itemIds.indexOf(this.currentItem?.itemId ?: 0)
        if (index < 0) null else index
    } catch (_: Exception) {
        null
    }
}

class SkipNextEpisodeController(val view: ImageView) : UIController() {
    init {
        view.setImageResource(R.drawable.ic_baseline_skip_next_24)
        view.setOnClickListener {
            remoteMediaClient?.let {
                it.queueNext(JSONObject())
                view.visibility = GONE // TO PREVENT MULTI CLICK
            }
        }
    }

    override fun onMediaStatusUpdated() {
        super.onMediaStatusUpdated()
        view.visibility = GONE
        val currentIdIndex = remoteMediaClient?.getItemIndex() ?: return
        val itemCount = remoteMediaClient?.mediaQueue?.itemCount ?: return
        if (itemCount - currentIdIndex > 1 && remoteMediaClient?.isLoadingNextItem == false) {
            view.visibility = VISIBLE
        }
    }
}

@Serializable
data class MetadataHolder(
    @JsonProperty("apiName") @SerialName("apiName") val apiName: String,
    @JsonProperty("isMovie") @SerialName("isMovie") val isMovie: Boolean,
    @JsonProperty("title") @SerialName("title") val title: String?,
    @JsonProperty("poster") @SerialName("poster") val poster: String?,
    @JsonProperty("currentEpisodeIndex") @SerialName("currentEpisodeIndex") val currentEpisodeIndex: Int,
    @JsonProperty("episodes") @SerialName("episodes") val episodes: List<ResultEpisode>,
    @JsonProperty("currentLinks") @SerialName("currentLinks") val currentLinks: List<ExtractorLink>,
    @JsonProperty("currentSubtitles") @SerialName("currentSubtitles") val currentSubtitles: List<SubtitleData>,
)

class SelectSourceController(val view: ImageView, val activity: ControllerActivity) : UIController() {
    init {
        view.setImageResource(R.drawable.ic_baseline_playlist_play_24)
        view.setOnClickListener {
            val holder = getCurrentMetaData()
            if (holder != null) {
                val items = holder.currentLinks
                if (items.isNotEmpty() && remoteMediaClient?.currentItem != null) {
                    val subTracks = remoteMediaClient?.mediaInfo?.mediaTracks?.filter { it.type == MediaTrack.TYPE_TEXT }
                        ?: ArrayList()

                    val bottomSheetDialogBuilder = AlertDialog.Builder(view.context, R.style.AlertDialogCustomBlack)
                    bottomSheetDialogBuilder.setView(R.layout.sort_bottom_sheet)

                    val bottomSheetDialog = bottomSheetDialogBuilder.create()
                    bottomSheetDialog.show()

                    val providerList = bottomSheetDialog.findViewById<ListView>(R.id.sort_providers)!!
                    val subtitleList = bottomSheetDialog.findViewById<ListView>(R.id.sort_subtitles)!!
                    if (subTracks.isEmpty()) {
                        bottomSheetDialog.findViewById<LinearLayout>(R.id.sort_subtitles_holder)?.visibility = GONE
                    } else {
                        val arrayAdapter = ArrayAdapter<String>(view.context, R.layout.sort_bottom_single_choice)
                        arrayAdapter.add(view.context.getString(R.string.no_subtitles))
                        arrayAdapter.addAll(subTracks.mapNotNull { it.name })

                        subtitleList.choiceMode = AbsListView.CHOICE_MODE_SINGLE
                        subtitleList.adapter = arrayAdapter

                        val currentTracks = remoteMediaClient?.mediaStatus?.activeTrackIds
                        val subtitleIndex = if (currentTracks == null) 0 else subTracks.map { it.id }
                            .indexOfFirst { currentTracks.contains(it) } + 1

                        subtitleList.setSelection(subtitleIndex)
                        subtitleList.setItemChecked(subtitleIndex, true)

                        subtitleList.setOnItemClickListener { _, _, which, _ ->
                            if (which == 0) {
                                remoteMediaClient?.setActiveMediaTracks(longArrayOf()) // NO SUBS
                            } else {
                                ChromecastSubtitlesFragment.getCurrentSavedStyle().apply {
                                    val font = TextTrackStyle()
                                    font.setFontFamily(fontFamily ?: "Google Sans")
                                    fontGenericFamily?.let { font.fontGenericFamily = it }
                                    font.windowColor = windowColor
                                    font.backgroundColor = backgroundColor

                                    font.edgeColor = edgeColor
                                    font.edgeType = edgeType
                                    font.foregroundColor = foregroundColor
                                    font.fontScale = fontScale

                                    remoteMediaClient?.setTextTrackStyle(font)
                                }

                                remoteMediaClient?.setActiveMediaTracks(longArrayOf(subTracks[which - 1].id))
                                    ?.setResultCallback {
                                        if (!it.status.isSuccess) {
                                            Log.e(
                                                "CHROMECAST", "Failed with status code:" +
                                                    it.status.statusCode + " > " + it.status.statusMessage
                                            )
                                        }
                                    }
                            }
                            bottomSheetDialog.dismissSafe(activity)
                        }
                    }

                    // https://developers.google.com/cast/docs/reference/web_receiver/cast.framework.messages.MediaInformation
                    val contentUrl = (remoteMediaClient?.currentItem?.media?.contentUrl
                        ?: remoteMediaClient?.currentItem?.media?.contentId)

                    val sortingMethods = items.map { "${it.name} ${Qualities.getStringByInt(it.quality)}" }
                        .toTypedArray()
                    val sotringIndex = items.indexOfFirst { it.url == contentUrl }

                    val arrayAdapter = ArrayAdapter<String>(view.context, R.layout.sort_bottom_single_choice)
                    arrayAdapter.addAll(sortingMethods.toMutableList())

                    providerList.choiceMode = AbsListView.CHOICE_MODE_SINGLE
                    providerList.adapter = arrayAdapter
                    providerList.setSelection(sotringIndex)
                    providerList.setItemChecked(sotringIndex, true)

                    providerList.setOnItemClickListener { _, _, which, _ ->
                        val epData = holder.episodes[holder.currentEpisodeIndex]
                        fun loadMirror(index: Int) {
                            if (holder.currentLinks.size <= index) return
                            val mediaItem = getMediaInfo(
                                epData,
                                holder,
                                index,
                                remoteMediaClient?.mediaInfo?.customData,
                                holder.currentSubtitles,
                            )

                            val startAt = remoteMediaClient?.approximateStreamPosition ?: 0
                            try { // THIS IS VERY IMPORTANT BECAUSE WE NEVER WANT TO AUTOLOAD THE NEXT EPISODE
                                val currentIdIndex = remoteMediaClient?.getItemIndex()
                                val nextId = remoteMediaClient?.mediaQueue?.itemIds?.get(
                                    currentIdIndex?.plus(1) ?: 0
                                )

                                if (currentIdIndex == null && nextId != null) {
                                    awaitLinks(
                                        remoteMediaClient?.queueInsertAndPlayItem(
                                            MediaQueueItem.Builder(mediaItem).build(),
                                            nextId,
                                            startAt,
                                            JSONObject(),
                                        )
                                    ) { loadMirror(index + 1) }
                                } else {
                                    val mediaLoadOptions =
                                        MediaLoadOptions.Builder()
                                            .setPlayPosition(startAt)
                                            .setAutoplay(true)
                                            .build()
                                    awaitLinks(
                                        remoteMediaClient?.load(
                                            mediaItem,
                                            mediaLoadOptions
                                        )
                                    ) { loadMirror(index + 1) }
                                }
                            } catch (_: Exception) {
                                val mediaLoadOptions =
                                    MediaLoadOptions.Builder()
                                        .setPlayPosition(startAt)
                                        .setAutoplay(true)
                                        .build()
                                awaitLinks(remoteMediaClient?.load(mediaItem, mediaLoadOptions)) {
                                    loadMirror(index + 1)
                                }
                            }
                        }

                        loadMirror(which)
                        bottomSheetDialog.dismissSafe(activity)
                    }
                }
            }
        }
    }

    private fun getCurrentMetaData(): MetadataHolder? {
        return try {
            val data = remoteMediaClient?.mediaInfo?.customData?.toString() ?: return null
            parseJson<MetadataHolder>(data)
        } catch (_: Exception) {
            null
        }
    }

    var isLoadingMore = false
    override fun onMediaStatusUpdated() {
        super.onMediaStatusUpdated()
        val meta = getCurrentMetaData()
        view.visibility = if ((meta?.currentLinks?.size ?: 0) > 1) VISIBLE else INVISIBLE

        try {
            if (meta != null && meta.episodes.size > meta.currentEpisodeIndex + 1) {
                val currentIdIndex = remoteMediaClient?.getItemIndex() ?: return
                val itemCount = remoteMediaClient?.mediaQueue?.itemCount
                val index = meta.currentEpisodeIndex + 1
                val epData = meta.episodes[index]

                try {
                    val currentDuration = remoteMediaClient?.streamDuration
                    val currentPosition = remoteMediaClient?.approximateStreamPosition
                    if (currentDuration != null && currentPosition != null)
                        DataStoreHelper.setViewPosAndResume(
                            epData.id,
                            currentPosition,
                            currentDuration,
                            epData,
                            meta.episodes.getOrNull(index + 1),
                        )
                } catch (t: Throwable) {
                    logError(t)
                }

                if (itemCount != null && itemCount - currentIdIndex == 1 && !isLoadingMore) {
                    isLoadingMore = true
                    ioSafe {
                        val currentLinks = mutableSetOf<ExtractorLink>()
                        val currentSubs = mutableSetOf<SubtitleData>()
                        val generator = RepoLinkGenerator(listOf(epData))
                        val isSuccessful = safeApiCall {
                            generator.generateLinks(
                                clearCache = false,
                                sourceTypes = LOADTYPE_CHROMECAST,
                                callback = {
                                    it.first?.let { link ->
                                        currentLinks.add(link)
                                    }
                                }, subtitleCallback = {
                                    currentSubs.add(it)
                                },
                                offset = 0,
                                isCasting = true,
                            )
                        }

                        val sortedLinks = sortUrls(currentLinks)
                        val sortedSubs = sortSubs(currentSubs)
                        if (isSuccessful == Resource.Success(true)) {
                            if (currentLinks.isNotEmpty()) {
                                val jsonCopy = meta.copy(
                                    currentLinks = sortedLinks,
                                    currentSubtitles = sortedSubs,
                                    currentEpisodeIndex = index,
                                )

                                val done = JSONObject(jsonCopy.toJson())
                                val mediaInfo = getMediaInfo(
                                    epData,
                                    jsonCopy,
                                    0,
                                    done,
                                    sortedSubs,
                                )

                                activity.runOnUiThread {
                                    awaitLinks(
                                        remoteMediaClient?.queueAppendItem(
                                            MediaQueueItem.Builder(mediaInfo).build(),
                                            JSONObject()
                                        )
                                    ) {
                                        println("FAILED TO LOAD NEXT ITEM")
                                    }
                                    isLoadingMore = false
                                }
                            }

                        }
                    }
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun onSessionConnected(castSession: CastSession) {
        super.onSessionConnected(castSession)
        remoteMediaClient?.queueSetRepeatMode(REPEAT_MODE_REPEAT_OFF, JSONObject())
    }
}

class SkipTimeController(val view: ImageView, forwards: Boolean) : UIController() {
    init {
        val time = 30
        view.setImageResource(if (forwards) R.drawable.go_forward_30 else R.drawable.go_back_30)
        view.setOnClickListener {
            remoteMediaClient?.let {
                val options = MediaSeekOptions.Builder()
                    .setPosition(it.approximateStreamPosition + time * 1000 * if (forwards) 1 else -1)
                it.seek(options.build())
            }
        }
    }
}

class ControllerActivity : ExpandedControllerActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.cast_expanded_controller_menu, menu)
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourcesButton: ImageView = getButtonImageViewAt(0)
        val skipBackButton: ImageView = getButtonImageViewAt(1)
        val skipForwardButton: ImageView = getButtonImageViewAt(2)
        val skipOpButton: ImageView = getButtonImageViewAt(3)
        uiMediaController.bindViewToUIController(
            sourcesButton,
            SelectSourceController(sourcesButton, this)
        )
        uiMediaController.bindViewToUIController(
            skipBackButton,
            SkipTimeController(skipBackButton, false)
        )
        uiMediaController.bindViewToUIController(
            skipForwardButton,
            SkipTimeController(skipForwardButton, true)
        )
        uiMediaController.bindViewToUIController(
            skipOpButton,
            SkipNextEpisodeController(skipOpButton)
        )
    }
}
