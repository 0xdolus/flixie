package com.dolus.flixie.extractors

import com.dolus.flixie.utils.*
import com.dolus.flixie.app
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.USER_AGENT
import com.dolus.flixie.newSubtitleFile
import com.dolus.flixie.utils.*

// import android.util.Log

class Uqload1 : Uqload() {
    override var mainUrl = "https://uqload.com"
}

class Uqload2 : Uqload() {
    override var mainUrl = "https://uqload.co"
}

class Uqloadcx : Uqload() {
    override var mainUrl = "https://uqload.cx"
}

class Uqloadbz : Uqload() {
    override var mainUrl = "https://uqload.bz"
}

open class Uqload : ExtractorApi() {
    override var name: String = "Uqload"
    override var mainUrl: String = "https://www.uqload.com"
    override val requiresReferer = true

    private val  srcRegex = Regex("""sources:.*"(.*?)".*""")  // would be possible to use the parse and find src attribute

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        with(app.get(url)) {  // raised error ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED (3003) is due to the response: "error_nofile"
            srcRegex.find(this.text)?.groupValues?.get(1)?.let { link ->
                // Log.d("CS3debugUQload","decoded URL: $link")
                callback.invoke(
                    newExtractorLink(
                        source = name,
                        name = name,
                        url = link
                    ) {
                        this.referer = "$mainUrl/"
                    }
                )
            }
        }
    }
}
