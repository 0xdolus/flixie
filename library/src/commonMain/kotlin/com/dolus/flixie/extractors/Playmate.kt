package com.dolus.flixie.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.flixie.Prerelease
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.ExtractorLinkType
import com.dolus.flixie.utils.newExtractorLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Prerelease
class Playmate : ExtractorApi() {
    override val name: String = "Playmate"
    override val mainUrl: String = "https://playmate.to"
    override val requiresReferer: Boolean = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfterLast("/")

        val resp = app.post(
            "$mainUrl/api/s",
            json = mapOf("c" to id, "d" to "web"),
            headers = mapOf("User-Agent" to "Mozilla/5.0 (X11; Linux x86_64; rv:153.0) Gecko/20100101 Firefox/153.0")
        ).parsed<StreamInfo>()

        callback.invoke(newExtractorLink(source = name, name = name, url = resp.sx, type = ExtractorLinkType.M3U8))
    }

    @Serializable
    private data class StreamInfo(
        @SerialName("sx")
        @JsonProperty("sx")
        val sx: String,
    )
}