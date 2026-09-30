package com.dolus.flixie.ui.search

import android.widget.Toast
import com.dolus.flixie.CommonActivity.activity
import com.dolus.flixie.CommonActivity.showToast
import com.dolus.flixie.MainActivity
import com.dolus.flixie.R
import com.dolus.flixie.ui.download.DOWNLOAD_ACTION_PLAY_FILE
import com.dolus.flixie.ui.download.DownloadButtonSetup.handleDownloadClick
import com.dolus.flixie.ui.download.DownloadClickEvent
import com.dolus.flixie.ui.result.START_ACTION_LOAD_EP
import com.dolus.flixie.utils.AppContextUtils.loadSearchResult
import com.dolus.flixie.utils.DataStoreHelper
import com.dolus.flixie.utils.downloader.DownloadObjects

object SearchHelper {
    fun handleSearchClickCallback(callback: SearchClickCallback) {
        val card = callback.card
        when (callback.action) {
            SEARCH_ACTION_LOAD -> {
                loadSearchResult(card)
            }

            SEARCH_ACTION_PLAY_FILE -> {
                if (card is DataStoreHelper.ResumeWatchingResult) {
                    val id = card.id
                    if (id == null) {
                        showToast(R.string.error_invalid_id, Toast.LENGTH_SHORT)
                    } else {
                        if (card.isFromDownload) {
                            handleDownloadClick(
                                DownloadClickEvent(
                                    DOWNLOAD_ACTION_PLAY_FILE,
                                    DownloadObjects.DownloadEpisodeCached(
                                        name = card.name,
                                        poster = card.posterUrl,
                                        episode = card.episode ?: 0,
                                        season = card.season,
                                        id = id,
                                        parentId = card.parentId ?: return,
                                        score = null,
                                        description = null,
                                        cacheTime = System.currentTimeMillis(),
                                    )
                                )
                            )
                        } else {
                            loadSearchResult(card, START_ACTION_LOAD_EP, id)
                        }
                    }
                } else {
                    handleSearchClickCallback(
                        SearchClickCallback(SEARCH_ACTION_LOAD, callback.view, -1, callback.card)
                    )
                }
            }

            SEARCH_ACTION_SHOW_METADATA -> {
                (activity as? MainActivity?)?.apply {
                    loadPopup(callback.card)
                } ?: kotlin.run {
                    showToast(callback.card.name, Toast.LENGTH_SHORT)
                }
            }
        }
    }
}