package com.dolus.flixie.extractors

import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.amap
import com.dolus.flixie.app
import com.dolus.flixie.extractors.helper.JwPlayerHelper
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.getAndUnpack
import com.dolus.flixie.utils.getPacked

open class Fastream : ExtractorApi() {
    override var mainUrl = "https://fastream.to"
    override var name = "Fastream"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val idregex = Regex("emb.html\\?(.*)=")
        val response = if (url.contains(Regex("(emb.html.*fastream)"))) {
            val id = idregex.find(url)?.destructured?.component1() ?: ""
            app.post(
                "$mainUrl/dl", allowRedirects = false,
                data = mapOf(
                    "op" to "embed",
                    "file_code" to id,
                    "auto" to "1"
                )
            ).document
        } else {
            app.get(url, referer = url).document
        }
        response.select("script").amap { script ->
            if (getPacked(script.data()) != null) {
                val unPacked = getAndUnpack(script.data())
                JwPlayerHelper.extractStreamLinks(unPacked, name, mainUrl, callback, subtitleCallback)
            }
        }
    }
}