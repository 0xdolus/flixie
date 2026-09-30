package com.dolus.flixie.actions.temp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.dolus.flixie.actions.OpenInAppAction
import com.dolus.flixie.actions.updateDurationAndPosition
import com.dolus.flixie.ui.result.LinkLoadingResult
import com.dolus.flixie.ui.result.ResultEpisode
import com.dolus.flixie.utils.txt
import com.dolus.flixie.utils.DataStoreHelper.getViewPos
import com.dolus.flixie.utils.ExtractorLinkType

class MpvKtPreviewPackage: MpvKtPackage(
    appName = "mpvKt Preview",
    packageName = "live.mehiz.mpvkt.preview",
)

open class MpvKtPackage(
    appName: String = "mpvKt",
    packageName: String = "live.mehiz.mpvkt",
): OpenInAppAction(
    appName = txt(appName),
    packageName = packageName,
    intentClass = "live.mehiz.mpvkt.ui.player.PlayerActivity"
) {
    override val oneSource = true

    override val sourceTypes = setOf(
        ExtractorLinkType.VIDEO,
        ExtractorLinkType.DASH,
        ExtractorLinkType.M3U8
    )

    override suspend fun putExtra(
        context: Context,
        intent: Intent,
        video: ResultEpisode,
        result: LinkLoadingResult,
        index: Int?
    ) {
        val link = result.links.getOrNull(index ?: 0) ?: return

        intent.apply {
            putExtra("subs", result.subs.map { it.url.toUri() }.toTypedArray())
            setDataAndType(link.url.toUri(), "video/*")

            // m3u8 plays, but changing sources feature is not available
            // makeTempM3U8Intent(activity, this, result)

            //putExtra("headers", link.headers.flatMap { listOf(it.key, it.value) }.toTypedArray())

            val position = getViewPos(video.id)?.position
            if (position != null)
                putExtra("position", position.toInt())

            putExtra("secure_uri", true)
        }
    }

    override fun onResult(activity: Activity, intent: Intent?) {
        val position = intent?.getIntExtra("position", -1)?.toLong() ?: -1
        val duration = intent?.getIntExtra("duration", -1)?.toLong() ?: -1
        updateDurationAndPosition(position, duration)
    }

}