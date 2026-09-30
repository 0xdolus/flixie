package com.dolus.flixie.actions.temp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.dolus.flixie.actions.OpenInAppAction
import com.dolus.flixie.ui.result.LinkLoadingResult
import com.dolus.flixie.ui.result.ResultEpisode
import com.dolus.flixie.utils.ExtractorLinkType
import com.dolus.flixie.utils.txt

/** https://github.com/proninyaroslav/libretorrent */
class LibreTorrentPackage : OpenInAppAction(
    appName = txt("LibreTorrent"),
    packageName = "org.proninyaroslav.libretorrent",
    intentClass = "org.proninyaroslav.libretorrent.ui.addtorrent.AddTorrentActivity"
) {
    // Only torrents are supported by the app
    override val sourceTypes: Set<ExtractorLinkType> =
        setOf(ExtractorLinkType.MAGNET, ExtractorLinkType.TORRENT)

    override val oneSource: Boolean = true

    override suspend fun putExtra(
        context: Context,
        intent: Intent,
        video: ResultEpisode,
        result: LinkLoadingResult,
        index: Int?
    ) {
        intent.data = result.links[index!!].url.toUri()
    }

    override fun onResult(activity: Activity, intent: Intent?) = Unit
}