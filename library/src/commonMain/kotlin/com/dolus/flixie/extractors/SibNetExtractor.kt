// ! Bu araç @keyiflerolsun tarafından | @KekikAkademi için yazılmıştır.

package com.dolus.flixie.extractors

import com.dolus.api.Log
import com.dolus.flixie.*
import com.dolus.flixie.utils.*

open class SibNet : ExtractorApi() {
    override val name            = "SibNet"
    override val mainUrl         = "https://video.sibnet.ru"
    override val requiresReferer = true

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        val extRef  = referer ?: ""
        val iSource = app.get(url, referer=extRef).text
        var m3uLink = Regex("""player.src\(\[\{src: \"([^\"]+)""").find(iSource)?.groupValues?.get(1) ?: throw ErrorLoadingException("m3u link not found")

        m3uLink = "${mainUrl}${m3uLink}"

        callback.invoke(
            newExtractorLink(
                source  = this.name,
                name    = this.name,
                url     = m3uLink,
            ) {
                this.referer = url
            }
        )
    }
}