package com.dolus.flixie.utils.videoskip

import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.flixie.LoadResponse
import com.dolus.flixie.LoadResponse.Companion.getImdbId
import com.dolus.flixie.LoadResponse.Companion.getTMDbId
import com.dolus.flixie.LoadResponse.Companion.isMovie
import com.dolus.flixie.TvType
import com.dolus.flixie.ui.result.ResultEpisode
import com.dolus.flixie.app
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** https://theintrodb.org/docs */
class TheIntroDBSkip : SkipAPI() {
    override val name = "TheIntroDB"
    override val supportedTypes = setOf(
        TvType.TvSeries, TvType.Cartoon, TvType.Anime, TvType.Movie,
        TvType.AsianDrama
    )

    val mainUrl = "https://api.theintrodb.org"

    override suspend fun stamps(
        data: LoadResponse,
        episode: ResultEpisode,
        episodeDurationMs: Long
    ): List<SkipStamp>? {
        val idSuffix =
            data.getTMDbId()?.let { tmdbId -> "tmdb_id=$tmdbId" }
                ?: data.getImdbId()?.let { imdbId -> "imdb_id=$imdbId" }
                ?: return null

        val url = if (data.isMovie()) {
            "$mainUrl/v2/media?$idSuffix"
        } else {
            val season = episode.season ?: return null
            "$mainUrl/v2/media?$idSuffix&season=$season&episode=${episode.episode}"
        }
        val root = app.get(url).parsed<Root>()
        return arrayOf(
            root.intro to SkipType.Intro,
            root.credits to SkipType.Credits,
            root.recap to SkipType.Recap,
            root.preview to SkipType.Preview
        ).map { (list, type) ->
            list.map { stamp ->
                SkipStamp(
                    type,
                    stamp.startMs ?: 0L,
                    stamp.endMs ?: episodeDurationMs
                )
            }
        }.flatten()
    }

    @Serializable
    data class Root(
        @JsonProperty("tmdb_id") @SerialName("tmdb_id") val tmdbId: Long,
        @JsonProperty("type") @SerialName("type") val type: String,
        @JsonProperty("intro") @SerialName("intro") val intro: List<Stamp> = emptyList(),
        @JsonProperty("recap") @SerialName("recap") val recap: List<Stamp> = emptyList(),
        @JsonProperty("credits") @SerialName("credits") val credits: List<Stamp> = emptyList(),
        @JsonProperty("preview") @SerialName("preview") val preview: List<Stamp> = emptyList(),
    )

    @Serializable
    data class Stamp(
        @JsonProperty("start_ms") @SerialName("start_ms") val startMs: Long?,
        @JsonProperty("end_ms") @SerialName("end_ms") val endMs: Long?,
    )
}
