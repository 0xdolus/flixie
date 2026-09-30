package com.dolus.flixie.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.flixie.APIHolder.getCaptchaToken
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.app
import com.dolus.flixie.utils.ExtractorApi
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.Qualities
import com.dolus.flixie.utils.newExtractorLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class VinovoSi : VinovoTo() {
    override val name = "VinovoSi"
    override val mainUrl = "https://vinovo.si"
}

open class VinovoTo : ExtractorApi() {
    override val mainUrl = "https://vinovo.to"
    override val name = "VinovoTo"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        val fixedUrl = url.replace("/d/", "/e/")
        val resp = app.get(fixedUrl, referer = referer)
        val doc = resp.document

        val videoBaseUrl = doc.selectFirst("video")?.attr("data-base") ?: return
        val videoToken = doc.selectFirst("meta[name=token]")?.attr("content") ?: return
        val fileCode = doc.selectFirst("meta[name=\"file_code\"]")?.attr("content") ?: return

        val captchaToken = doc.selectFirst("meta[name=recaptcha]")?.attr("content") ?: return
        val captchaSolution = getCaptchaToken(fixedUrl, captchaToken, "$mainUrl/") ?: return

        val streamInfo = app.post(
            url = "$mainUrl/api/file/url/$fileCode",
            data = mapOf("token" to videoToken, "recaptcha" to captchaSolution),
            headers = mapOf(
                "Origin" to mainUrl,
                "X-Requested-With" to "XMLHttpRequest",
            ),
            cookies = resp.cookies,
            referer = fixedUrl,
        ).parsed<VinovoFileResp>()

        val fileUrl = "$videoBaseUrl/stream/${streamInfo.token}"
        callback.invoke(
            newExtractorLink(source = name, name = name, url = fileUrl) {
                val dataTitle = doc.selectFirst("video")?.attr("data-title").orEmpty()
                quality = Regex("""\.(\d+)p\.""").find(dataTitle)?.groupValues?.getOrNull(1)
                    ?.toIntOrNull() ?: Qualities.Unknown.value
            }
        )
    }

    @Serializable
    private data class VinovoFileResp(
        @JsonProperty("status") @SerialName("status") val status: String,
        @JsonProperty("token") @SerialName("token") val token: String,
    )
}
