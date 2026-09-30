package com.dolus.flixie.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.api.Log
import com.dolus.flixie.Prerelease
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.newExtractorLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Prerelease
class Hexload: ExtractorApi() {
    override val name: String = "Hexload"
    override val mainUrl: String = "https://hexload.com"
    override val requiresReferer: Boolean = false

    private val jsDictEntryRegex = Regex("""(\w+):\s*['"](\w+)['"]""")

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val respData = app.get(url).text.substringAfter("data: {").substringBefore("success")
        val requestArgs = jsDictEntryRegex.findAll(respData).associate {
            it.groupValues[1] to it.groupValues[2]
        }
        Log.d("requestArgs", requestArgs.toString())
        val streamResponse = app.post("$mainUrl/download", data = requestArgs)
            .parsed<StreamResponse>()

        callback.invoke(newExtractorLink(
            name = name,
            source = name,
            url = streamResponse.result.url
        ))
    }

    @Serializable
    private data class StreamResponse(
        val result: StreamResult,
        @JsonProperty("server_time")
        @SerialName("server_time")
        val serverTime: String,
        val msg: String,
        val status: Long,
    )

    @Serializable
    private data class StreamResult(
        val size: String,
        val url: String,
        @JsonProperty("content_type")
        @SerialName("content_type")
        val contentType: String,
        @JsonProperty("thumb_url")
        @SerialName("thumb_url")
        val thumbUrl: String,
        @JsonProperty("image_url")
        @SerialName("image_url")
        val imageUrl: String,
        @JsonProperty("file_name")
        @SerialName("file_name")
        val fileName: String,
        val md5: String,
    )
}
