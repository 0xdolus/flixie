package com.dolus.flixie.extractors.helper

import com.dolus.api.Log
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.amap
import com.dolus.flixie.app
import com.dolus.flixie.utils.ExtractorLink
import com.dolus.flixie.utils.loadExtractor

class AsianEmbedHelper {
    companion object {
        suspend fun getUrls(
            url: String,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
        ) {
            // Fetch links
            val doc = app.get(url).document
            val links = doc.select("div#list-server-more > ul > li.linkserver")
            if (!links.isNullOrEmpty()) {
                links.amap {
                    val datavid = it.attr("data-video")
                    //Log.i("AsianEmbed", "Result => (datavid) ${datavid}")
                    if (datavid.isNotBlank()) {
                        val res = loadExtractor(datavid, url, subtitleCallback, callback)
                        Log.i("AsianEmbed", "Result => ($res) (datavid) $datavid")
                    }
                }
            }
        }
    }
}