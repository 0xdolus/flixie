package com.dolus.flixie.extractors

import com.dolus.flixie.Prerelease
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.newExtractorLink

@Prerelease
open class HubuCloud: ExtractorApi() {
    override val name: String = "Hubu"
    override val mainUrl: String = "https://hubu.cloud"
    override val requiresReferer: Boolean = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val doc = app.get(url).document

        val streamUrl = doc.select("source").attr("src")
        callback.invoke(newExtractorLink(source = name, name = name, url = streamUrl))
    }
}