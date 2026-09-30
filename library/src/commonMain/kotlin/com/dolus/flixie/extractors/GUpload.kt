package com.dolus.flixie.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.base64Decode
import com.dolus.flixie.utils.AppUtils.parseJson
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.getQualityFromName
import com.dolus.flixie.utils.newExtractorLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

open class GUpload: ExtractorApi() {
    override val name: String = "GUpload"
    override val mainUrl: String = "https://gupload.xyz"
    override val requiresReferer: Boolean = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        val response = app.get(url, referer = referer).text

        val playerConfigString = response.substringAfter("const config = ").substringBefore(";")
        val playerConfig = parseJson<VideoInfo>(playerConfigString)

        callback.invoke(
            newExtractorLink(
                source = name,
                name = name,
                url = playerConfig.videoUrl,
            ) {
                Regex("/(\\d+p)\\.").find(playerConfig.videoUrl)?.groupValues?.get(1)?.let {
                    quality = getQualityFromName(it)
                }
            }
        )
    }

    @Serializable
    private data class VideoInfo(
        @JsonProperty("videoUrl") @SerialName("videoUrl") val videoUrl: String,
        @JsonProperty("posterUrl") @SerialName("posterUrl") val posterUrl: String? = null,
        @JsonProperty("videoId") @SerialName("videoId") val videoId: String? = null,
        @JsonProperty("primaryColor") @SerialName("primaryColor") val primaryColor: String? = null,
        @JsonProperty("audioTracks") @SerialName("audioTracks") val audioTracks: List<JsonElement> = emptyList(),
        @JsonProperty("subtitleTracks") @SerialName("subtitleTracks") val subtitleTracks: List<JsonElement> = emptyList(),
        @JsonProperty("vastFallbackList") @SerialName("vastFallbackList") val vastFallbackList: List<String> = emptyList(),
        @JsonProperty("videoOwnerId") @SerialName("videoOwnerId") val videoOwnerId: Long = 0,
    )
}
