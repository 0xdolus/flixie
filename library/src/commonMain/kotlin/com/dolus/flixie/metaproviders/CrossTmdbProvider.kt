package com.dolus.flixie.metaproviders

import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.flixie.APIHolder.apis
import com.dolus.flixie.APIHolder.getApiFromNameNull
import com.dolus.flixie.ErrorLoadingException
import com.dolus.flixie.LoadResponse
import com.dolus.flixie.MovieLoadResponse
import com.dolus.flixie.MovieSearchResponse
import com.dolus.flixie.SearchResponseList
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.TvType
import com.dolus.flixie.amap
import com.dolus.flixie.mvvm.logError
import com.dolus.flixie.toNewSearchResponseList
import com.dolus.flixie.utils.AppUtils.toJson
import com.dolus.flixie.utils.AppUtils.tryParseJson
import com.dolus.flixie.utils.ExtractorLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class CrossTmdbProvider : TmdbProvider() {
    override var name = "MultiMovie"
    override val apiName = "MultiMovie"
    override var lang = "en"
    override val useMetaLoadResponse = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Movie)

    private fun filterName(name: String): String {
        return Regex("""[^a-zA-Z0-9-]""").replace(name, "")
    }

    private val validApis
        get() = apis.filter { it.lang == this.lang && it::class != this::class }
    //.distinctBy { it.uniqueId }

    @Serializable
    data class CrossMetaData(
        @JsonProperty("isSuccess") @SerialName("isSuccess") val isSuccess: Boolean,
        @JsonProperty("movies") @SerialName("movies") val movies: List<Pair<String, String>>? = null,
    )

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        tryParseJson<CrossMetaData>(data)?.let { metaData ->
            if (!metaData.isSuccess) return false
            metaData.movies?.amap { (apiName, data) ->
                getApiFromNameNull(apiName)?.let {
                    try {
                        it.loadLinks(data, isCasting, subtitleCallback, callback)
                    } catch (e: Exception) {
                        logError(e)
                    }
                }
            }

            return true
        }
        return false
    }

    override suspend fun search(query: String, page: Int): SearchResponseList? {
        // TODO REMOVE
        return super.search(query, page)
            ?.items
            ?.filterIsInstance<MovieSearchResponse>()
            ?.toNewSearchResponseList()
    }

    override suspend fun load(url: String): LoadResponse? {
        val base = super.load(url)?.apply {
            this.recommendations =
                this.recommendations?.filterIsInstance<MovieSearchResponse>() // TODO REMOVE
            val matchName = filterName(this.name)
            when (this) {
                is MovieLoadResponse -> {
                    val data = validApis.amap { api ->
                        try {
                            if (api.supportedTypes.contains(TvType.Movie)) { //|| api.supportedTypes.contains(TvType.AnimeMovie)
                                return@amap api.search(this.name)?.first {
                                    if (filterName(it.name).equals(
                                            matchName,
                                            ignoreCase = true
                                        )
                                    ) {
                                        if (it is MovieSearchResponse)
                                            if (it.year != null && this.year != null && it.year != this.year) // if year exist then do a check
                                                return@first false

                                        return@first true
                                    }
                                    false
                                }?.let { search ->
                                    val response = api.load(search.url)
                                    if (response is MovieLoadResponse) {
                                        response
                                    } else {
                                        null
                                    }
                                }
                            }
                            null
                        } catch (e: Exception) {
                            logError(e)
                            null
                        }
                    }.filterNotNull()
                    this.dataUrl =
                        CrossMetaData(true, data.map { it.apiName to it.dataUrl }).toJson()
                }

                else -> {
                    throw ErrorLoadingException("Nothing besides movies are implemented for this provider")
                }
            }
        }

        return base
    }
}
