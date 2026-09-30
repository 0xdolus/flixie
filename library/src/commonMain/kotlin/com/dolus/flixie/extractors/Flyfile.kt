package com.dolus.flixie.extractors

import com.dolus.flixie.Prerelease
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.ExtractorLinkType
import com.dolus.flixie.utils.newExtractorLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

open class Flyfile : ExtractorApi() {
    override val name = "FlyFile"
    override val mainUrl = "https://flyfile.app"
    override val requiresReferer = false
    open val apiUrl: String = "https://api.flyfile.app"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        val videoId = url.substringAfterLast("/")
        val videoInfo = app.get("$apiUrl/api/streaming/assign/$videoId")
            .parsed<StreamInfo>()

        val streamUrl = "${videoInfo.url}/hls/${videoInfo.token}/master.m3u8"
        callback.invoke(
            newExtractorLink(
                source = name,
                name = name,
                url = streamUrl,
                type = ExtractorLinkType.M3U8,
            )
        )
    }

    @Serializable
    private data class StreamInfo(
        @SerialName("url") val url: String,
        @SerialName("token") val token: String,
    )
}
