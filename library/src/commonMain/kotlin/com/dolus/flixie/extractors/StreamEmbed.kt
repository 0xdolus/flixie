package com.dolus.flixie.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.utils.AppUtils.parseJson
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.M3u8Helper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

open class StreamEmbed : ExtractorApi() {
    override val name = "StreamEmbed"
    override val mainUrl = "https://watch.gxplayer.xyz"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        val jsonString = app.get(url, referer = mainUrl).text
            .substringAfter("var video = ").substringBefore(";")
        val video = parseJson<Details>(jsonString)
        M3u8Helper.generateM3u8(
            this.name,
            "$mainUrl/m3u8/${video.uid}/${video.md5}/master.txt?s=1&id=${video.id}&cache=${video.status}",
            referer = "$mainUrl/",
        ).forEach(callback)
    }

    @Serializable
    private data class Details(
        @JsonProperty("id") @SerialName("id") val id: String,
        @JsonProperty("uid") @SerialName("uid") val uid: String,
        @JsonProperty("slug") @SerialName("slug") val slug: String,
        @JsonProperty("title") @SerialName("title") val title: String,
        @JsonProperty("quality") @SerialName("quality") val quality: String,
        @JsonProperty("type") @SerialName("type") val type: String,
        @JsonProperty("status") @SerialName("status") val status: String,
        @JsonProperty("md5") @SerialName("md5") val md5: String,
    )
}
