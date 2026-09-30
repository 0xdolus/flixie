package com.dolus.flixie.extractors

import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.extractors.helper.JwPlayerHelper
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.JsUnpacker

open class Supervideo : ExtractorApi() {
    override var name = "Supervideo"
    override var mainUrl = "https://supervideo.cc"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url).text
        val jstounpack = Regex("eval((.|\\n)*?)</script>").find(response)?.groups?.get(1)?.value
        val unpacked = JsUnpacker(jstounpack).unpack()

        JwPlayerHelper.extractStreamLinks(unpacked.orEmpty(), name, mainUrl, callback, subtitleCallback)
    }
}