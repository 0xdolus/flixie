package com.dolus.flixie.extractors

import com.dolus.flixie.Prerelease
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.ExtractorLinkType
import com.dolus.flixie.utils.newExtractorLink

open class Streamcash: ExtractorApi() {
    override val name: String = "Streamcash"
    override val mainUrl: String = "https://streamcash.to"
    open val cdnUrl: String = "https://cdn.streamcash.to"
    override val requiresReferer: Boolean = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.removeSuffix("/").substringAfterLast("/")
        callback.invoke(
            newExtractorLink(
                name = name,
                source = name,
                url = "$cdnUrl/videos/$id/index.m3u8",
                type = ExtractorLinkType.M3U8
            )
        )
    }
}