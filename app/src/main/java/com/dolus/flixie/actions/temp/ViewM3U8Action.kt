package com.dolus.flixie.actions.temp

import android.content.Context
import android.content.Intent
import com.dolus.flixie.R
import com.dolus.flixie.actions.VideoClickAction
import com.dolus.flixie.actions.makeTempM3U8Intent
import com.dolus.flixie.ui.result.LinkLoadingResult
import com.dolus.flixie.ui.result.ResultEpisode
import com.dolus.flixie.utils.txt

class ViewM3U8Action: VideoClickAction() {
    override val name = txt(R.string.episode_action_play_in_format, "m3u8 player")

    override val isPlayer = true

    override fun shouldShow(context: Context?, video: ResultEpisode?) = true

    override suspend fun runAction(
        context: Context?,
        video: ResultEpisode,
        result: LinkLoadingResult,
        index: Int?
    ) {
        if (context == null) return
        val i = Intent(Intent.ACTION_VIEW)
        makeTempM3U8Intent(context, i, result)
        launch(i)
    }
}