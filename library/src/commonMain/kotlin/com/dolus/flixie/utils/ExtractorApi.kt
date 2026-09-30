package com.dolus.flixie.utils

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fleeksoft.ksoup.Ksoup
import com.dolus.flixie.AudioFile
import com.dolus.flixie.IDownloadableMinimum
import com.dolus.flixie.Prerelease
import com.dolus.flixie.SubtitleFile
import com.dolus.flixie.USER_AGENT
import com.dolus.flixie.app
import com.dolus.flixie.extractors.Acefile
import com.dolus.flixie.extractors.Ahvsh
import com.dolus.flixie.extractors.Aico
import com.dolus.flixie.extractors.Antarcticadocs
import com.dolus.flixie.extractors.Asnwish
import com.dolus.flixie.extractors.Auvexiug
import com.dolus.flixie.extractors.Awish
import com.dolus.flixie.extractors.BgwpCC
import com.dolus.flixie.extractors.BigwarpArt
import com.dolus.flixie.extractors.BigwarpIO
import com.dolus.flixie.extractors.Blogger
import com.dolus.flixie.extractors.ByseSX
import com.dolus.flixie.extractors.Bysezejataos
import com.dolus.flixie.extractors.ByseBuho
import com.dolus.flixie.extractors.ByseVepoin
import com.dolus.flixie.extractors.ByseQekaho
import com.dolus.flixie.extractors.Cavanhabg
import com.dolus.flixie.extractors.Cda
import com.dolus.flixie.extractors.Cdnplayer
import com.dolus.flixie.extractors.CdnwishCom
import com.dolus.flixie.extractors.CloudMailRu
import com.dolus.flixie.extractors.ContentX
import com.dolus.flixie.extractors.CsstOnline
import com.dolus.flixie.extractors.D0000d
import com.dolus.flixie.extractors.D000dCom
import com.dolus.flixie.extractors.DBfilm
import com.dolus.flixie.extractors.Dailymotion
import com.dolus.flixie.extractors.DatabaseGdrive
import com.dolus.flixie.extractors.DatabaseGdrive2
import com.dolus.flixie.extractors.DesuArcg
import com.dolus.flixie.extractors.DesuDrive
import com.dolus.flixie.extractors.DesuOdchan
import com.dolus.flixie.extractors.DesuOdvip
import com.dolus.flixie.extractors.Dhcplay
import com.dolus.flixie.extractors.Dhtpre
import com.dolus.flixie.extractors.Dokicloud
import com.dolus.flixie.extractors.DoodCxExtractor
import com.dolus.flixie.extractors.DoodLaExtractor
import com.dolus.flixie.extractors.DoodPmExtractor
import com.dolus.flixie.extractors.DoodShExtractor
import com.dolus.flixie.extractors.DoodSoExtractor
import com.dolus.flixie.extractors.DoodToExtractor
import com.dolus.flixie.extractors.DoodWatchExtractor
import com.dolus.flixie.extractors.DoodWfExtractor
import com.dolus.flixie.extractors.DoodWsExtractor
import com.dolus.flixie.extractors.DoodYtExtractor
import com.dolus.flixie.extractors.Doodspro
import com.dolus.flixie.extractors.Dsvplay
import com.dolus.flixie.extractors.Doodporn
import com.dolus.flixie.extractors.DoodstreamCom
import com.dolus.flixie.extractors.Dooood
import com.dolus.flixie.extractors.Ds2play
import com.dolus.flixie.extractors.Ds2video
import com.dolus.flixie.extractors.DsstOnline
import com.dolus.flixie.extractors.Dumbalag
import com.dolus.flixie.extractors.Dwish
import com.dolus.flixie.extractors.Embedgram
import com.dolus.flixie.extractors.EmturbovidExtractor
import com.dolus.flixie.extractors.Evoload
import com.dolus.flixie.extractors.Evoload1
import com.dolus.flixie.extractors.Ewish
import com.dolus.flixie.extractors.FEmbed
import com.dolus.flixie.extractors.FEnet
import com.dolus.flixie.extractors.Fastream
import com.dolus.flixie.extractors.FeHD
import com.dolus.flixie.extractors.Fembed9hd
import com.dolus.flixie.extractors.FileMoon
import com.dolus.flixie.extractors.FileMoonIn
import com.dolus.flixie.extractors.FileMoonSx
import com.dolus.flixie.extractors.FilemoonV2
import com.dolus.flixie.extractors.Filesim
import com.dolus.flixie.extractors.Firestream
import com.dolus.flixie.extractors.FirestreamSite
import com.dolus.flixie.extractors.Multimoviesshg
import com.dolus.flixie.extractors.FlaswishCom
import com.dolus.flixie.extractors.Flyfile
import com.dolus.flixie.extractors.FourCX
import com.dolus.flixie.extractors.FourPichive
import com.dolus.flixie.extractors.FourPlayRu
import com.dolus.flixie.extractors.Fplayer
import com.dolus.flixie.extractors.FsstOnline
import com.dolus.flixie.extractors.GDMirrorbot
import com.dolus.flixie.extractors.GUpload
import com.dolus.flixie.extractors.GamoVideo
import com.dolus.flixie.extractors.Gdriveplayer
import com.dolus.flixie.extractors.Gdriveplayerapi
import com.dolus.flixie.extractors.Gdriveplayerapp
import com.dolus.flixie.extractors.Gdriveplayerbiz
import com.dolus.flixie.extractors.Gdriveplayerco
import com.dolus.flixie.extractors.Gdriveplayerfun
import com.dolus.flixie.extractors.Gdriveplayerio
import com.dolus.flixie.extractors.Gdriveplayerme
import com.dolus.flixie.extractors.Gdriveplayerorg
import com.dolus.flixie.extractors.Gdriveplayerus
import com.dolus.flixie.extractors.Geodailymotion
import com.dolus.flixie.extractors.Gofile
import com.dolus.flixie.extractors.GoodstreamExtractor
import com.dolus.flixie.extractors.Guccihide
import com.dolus.flixie.extractors.Guxhag
import com.dolus.flixie.extractors.HDMomPlayer
import com.dolus.flixie.extractors.HDPlayerSystem
import com.dolus.flixie.extractors.HDStreamAble
import com.dolus.flixie.extractors.Habetar
import com.dolus.flixie.extractors.Handfacesnap
import com.dolus.flixie.extractors.Haxloppd
import com.dolus.flixie.extractors.Hexload
import com.dolus.flixie.extractors.Hgcloudto
import com.dolus.flixie.extractors.HglinkTo
import com.dolus.flixie.extractors.HgplayCDN
import com.dolus.flixie.extractors.Hotlinger
import com.dolus.flixie.extractors.HubCloud
import com.dolus.flixie.extractors.Hxfile
import com.dolus.flixie.extractors.HlsWish
import com.dolus.flixie.extractors.HubuCloud
import com.dolus.flixie.extractors.InternetArchive
import com.dolus.flixie.extractors.JWPlayer
import com.dolus.flixie.extractors.Jeniusplay
import com.dolus.flixie.extractors.Jodwish
import com.dolus.flixie.extractors.Keephealth
import com.dolus.flixie.extractors.KotakAnimeid
import com.dolus.flixie.extractors.Kotakajair
import com.dolus.flixie.extractors.Krakenfiles
import com.dolus.flixie.extractors.Kswplayer
import com.dolus.flixie.extractors.LayarKaca
import com.dolus.flixie.extractors.Linkbox
import com.dolus.flixie.extractors.LuluStream
import com.dolus.flixie.extractors.Lulustream1
import com.dolus.flixie.extractors.Lulustream2
import com.dolus.flixie.extractors.Luluvdoo
import com.dolus.flixie.extractors.Luxubu
import com.dolus.flixie.extractors.Lvturbo
import com.dolus.flixie.extractors.MailRu
import com.dolus.flixie.extractors.Maxstream
import com.dolus.flixie.extractors.Mediafire
import com.dolus.flixie.extractors.Megacloud
import com.dolus.flixie.extractors.Meownime
import com.dolus.flixie.extractors.MetaGnathTuggers
import com.dolus.flixie.extractors.MixDrop
import com.dolus.flixie.extractors.MixDropAg
import com.dolus.flixie.extractors.MixDropBz
import com.dolus.flixie.extractors.MixDropCh
import com.dolus.flixie.extractors.MixDropTo
import com.dolus.flixie.extractors.MixDropPs
import com.dolus.flixie.extractors.Mdy
import com.dolus.flixie.extractors.MixDropSi
import com.dolus.flixie.extractors.MxDropTo
import com.dolus.flixie.extractors.Movhide
import com.dolus.flixie.extractors.Moviehab
import com.dolus.flixie.extractors.MoviehabNet
import com.dolus.flixie.extractors.Moviesm4u
import com.dolus.flixie.extractors.Mp4Upload
import com.dolus.flixie.extractors.Multimovies
import com.dolus.flixie.extractors.Mvidoo
import com.dolus.flixie.extractors.MyVidPlay
import com.dolus.flixie.extractors.Mwish
import com.dolus.flixie.extractors.Namefacesnap
import com.dolus.flixie.extractors.Nameitweb
import com.dolus.flixie.extractors.NathanFromSubject
import com.dolus.flixie.extractors.Nekostream
import com.dolus.flixie.extractors.Nekowish
import com.dolus.flixie.extractors.Neonime7n
import com.dolus.flixie.extractors.Neonime8n
import com.dolus.flixie.extractors.Obeywish
import com.dolus.flixie.extractors.Odnoklassniki
import com.dolus.flixie.extractors.Odysseusa
import com.dolus.flixie.extractors.OkRuHTTP
import com.dolus.flixie.extractors.OkRuHTTPMobile
import com.dolus.flixie.extractors.OkRuSSL
import com.dolus.flixie.extractors.OkRuSSLMobile
import com.dolus.flixie.extractors.PeaceMakerst
import com.dolus.flixie.extractors.Peytonepre
import com.dolus.flixie.extractors.Pichive
import com.dolus.flixie.extractors.PixelDrain
import com.dolus.flixie.extractors.PixelDrainDev
import com.dolus.flixie.extractors.PlayLtXyz
import com.dolus.flixie.extractors.PlayRu
import com.dolus.flixie.extractors.PlayerVoxzer
import com.dolus.flixie.extractors.Playerwish
import com.dolus.flixie.extractors.Playmate
import com.dolus.flixie.extractors.Playmogo
import com.dolus.flixie.extractors.Rabbitstream
import com.dolus.flixie.extractors.RapidVid
import com.dolus.flixie.extractors.Rasacintaku
import com.dolus.flixie.extractors.SBfull
import com.dolus.flixie.extractors.Sbasian
import com.dolus.flixie.extractors.Sbface
import com.dolus.flixie.extractors.Sbflix
import com.dolus.flixie.extractors.Sblona
import com.dolus.flixie.extractors.Sblongvu
import com.dolus.flixie.extractors.Sbnet
import com.dolus.flixie.extractors.Sbrapid
import com.dolus.flixie.extractors.Sbsonic
import com.dolus.flixie.extractors.Sbspeed
import com.dolus.flixie.extractors.Sbthe
import com.dolus.flixie.extractors.SecvideoOnline
import com.dolus.flixie.extractors.Sendvid
import com.dolus.flixie.extractors.Server1uns
import com.dolus.flixie.extractors.SfastwishCom
import com.dolus.flixie.extractors.ShaveTape
import com.dolus.flixie.extractors.SibNet
import com.dolus.flixie.extractors.Simpulumlamerop
import com.dolus.flixie.extractors.Smoothpre
import com.dolus.flixie.extractors.Sobreatsesuyp
import com.dolus.flixie.extractors.Ssbstream
import com.dolus.flixie.extractors.StreamEmbed
import com.dolus.flixie.extractors.StreamHLS
import com.dolus.flixie.extractors.StreamM4u
import com.dolus.flixie.extractors.StreamSB
import com.dolus.flixie.extractors.StreamSB1
import com.dolus.flixie.extractors.StreamSB10
import com.dolus.flixie.extractors.StreamSB11
import com.dolus.flixie.extractors.StreamSB2
import com.dolus.flixie.extractors.StreamSB3
import com.dolus.flixie.extractors.StreamSB4
import com.dolus.flixie.extractors.StreamSB5
import com.dolus.flixie.extractors.StreamSB6
import com.dolus.flixie.extractors.StreamSB7
import com.dolus.flixie.extractors.StreamSB8
import com.dolus.flixie.extractors.StreamSB9
import com.dolus.flixie.extractors.StreamSilk
import com.dolus.flixie.extractors.StreamTape
import com.dolus.flixie.extractors.StreamTapeNet
import com.dolus.flixie.extractors.StreamTapeXyz
import com.dolus.flixie.extractors.Watchadsontape
import com.dolus.flixie.extractors.StreamWishExtractor
import com.dolus.flixie.extractors.StreamhideCom
import com.dolus.flixie.extractors.StreamhideTo
import com.dolus.flixie.extractors.Streamhub2
import com.dolus.flixie.extractors.Streamlare
import com.dolus.flixie.extractors.StreamoUpload
import com.dolus.flixie.extractors.Streamplay
import com.dolus.flixie.extractors.Streamsss
import com.dolus.flixie.extractors.Streamwish2
import com.dolus.flixie.extractors.Strwish
import com.dolus.flixie.extractors.Strwish2
import com.dolus.flixie.extractors.Supervideo
import com.dolus.flixie.extractors.Swdyu
import com.dolus.flixie.extractors.Swhoi
import com.dolus.flixie.extractors.TRsTX
import com.dolus.flixie.extractors.Tantifilm
import com.dolus.flixie.extractors.TauVideo
import com.dolus.flixie.extractors.Techinmind
import com.dolus.flixie.extractors.Tubeless
import com.dolus.flixie.extractors.Uasopt
import com.dolus.flixie.extractors.Up4FunTop
import com.dolus.flixie.extractors.Up4Stream
import com.dolus.flixie.extractors.Upstream
import com.dolus.flixie.extractors.UpstreamExtractor
import com.dolus.flixie.extractors.Uqload
import com.dolus.flixie.extractors.Uqload1
import com.dolus.flixie.extractors.Uqload2
import com.dolus.flixie.extractors.Uqloadcx
import com.dolus.flixie.extractors.Uqloadbz
import com.dolus.flixie.extractors.UqloadsXyz
import com.dolus.flixie.extractors.Urochsunloath
import com.dolus.flixie.extractors.Userload
import com.dolus.flixie.extractors.Userscloud
import com.dolus.flixie.extractors.Uservideo
import com.dolus.flixie.extractors.Videa
import com.dolus.flixie.extractors.Vicloud
import com.dolus.flixie.extractors.VidHidePro
import com.dolus.flixie.extractors.VidHidePro1
import com.dolus.flixie.extractors.VidHidePro2
import com.dolus.flixie.extractors.VidHidePro3
import com.dolus.flixie.extractors.VidHidePro4
import com.dolus.flixie.extractors.VidHidePro5
import com.dolus.flixie.extractors.VidHidePro6
import com.dolus.flixie.extractors.VidHideHub
import com.dolus.flixie.extractors.Ryderjet
import com.dolus.flixie.extractors.Streamcash
import com.dolus.flixie.extractors.Thebesthostertv
import com.dolus.flixie.extractors.VidChampions
import com.dolus.flixie.extractors.VidMoxy
import com.dolus.flixie.extractors.VidStack
import com.dolus.flixie.extractors.VideoSeyred
import com.dolus.flixie.extractors.Videzz
import com.dolus.flixie.extractors.Vidgomunime
import com.dolus.flixie.extractors.Vidgomunimesb
import com.dolus.flixie.extractors.VidhideExtractor
import com.dolus.flixie.extractors.Vidmoly
import com.dolus.flixie.extractors.Vidmolyme
import com.dolus.flixie.extractors.Vidmolyto
import com.dolus.flixie.extractors.Vidmolybiz
import com.dolus.flixie.extractors.Vido
import com.dolus.flixie.extractors.Vidoza
import com.dolus.flixie.extractors.VinovoSi
import com.dolus.flixie.extractors.VinovoTo
import com.dolus.flixie.extractors.VidNest
import com.dolus.flixie.extractors.VidaaraxCom
import com.dolus.flixie.extractors.VidaaraxNet
import com.dolus.flixie.extractors.Vidara
import com.dolus.flixie.extractors.VidaraSo
import com.dolus.flixie.extractors.Vidaraa
import com.dolus.flixie.extractors.Vidaratem
import com.dolus.flixie.extractors.Vidaraw
import com.dolus.flixie.extractors.Vidarax
import com.dolus.flixie.extractors.Vidavaca
import com.dolus.flixie.extractors.Vide0Net
import com.dolus.flixie.extractors.Vidmatrixa
import com.dolus.flixie.extractors.Vids
import com.dolus.flixie.extractors.Vidsonic
import com.dolus.flixie.extractors.Vixeo
import com.dolus.flixie.extractors.VkExtractor
import com.dolus.flixie.extractors.Voe
import com.dolus.flixie.extractors.Voe1
import com.dolus.flixie.extractors.Voe2
import com.dolus.flixie.extractors.Vtbe
import com.dolus.flixie.extractors.Wibufile
import com.dolus.flixie.extractors.WishembedPro
import com.dolus.flixie.extractors.Wishfast
import com.dolus.flixie.extractors.Wishonly
import com.dolus.flixie.extractors.XStreamCdn
import com.dolus.flixie.extractors.Xenolyzb
import com.dolus.flixie.extractors.Yipsu
import com.dolus.flixie.extractors.YourUpload
import com.dolus.flixie.extractors.YoutubeExtractor
import com.dolus.flixie.extractors.YoutubeMobileExtractor
import com.dolus.flixie.extractors.YoutubeNoCookieExtractor
import com.dolus.flixie.extractors.YoutubeShortLinkExtractor
import com.dolus.flixie.extractors.Yufiles
import com.dolus.flixie.extractors.Yuguaab
import com.dolus.flixie.extractors.Zplayer
import com.dolus.flixie.extractors.ZplayerV2
import com.dolus.flixie.extractors.Ztreamhub
import com.dolus.flixie.mvvm.logError
import com.dolus.flixie.utils.Coroutines.atomicListOf
import io.ktor.http.Url
import io.ktor.http.decodeURLPart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

/**
 * For use in the ConcatenatingMediaSource.
 * If features are missing (headers), please report and we can add it.
 * @param durationUs use Long.toUs() for easier input
 * */
data class PlayListItem(
    val url: String,
    val durationUs: Long,
)

/**
 * Converts Seconds to MicroSeconds, multiplication by 1_000_000
 * */
fun Long.toUs(): Long {
    return this * 1_000_000
}

/**
 * If your site has an unorthodox m3u8-like system where there are multiple smaller videos concatenated
 * use this.
 * */
@Suppress("DEPRECATION")
data class ExtractorLinkPlayList(
    override val source: String,
    override val name: String,
    val playlist: List<PlayListItem>,
    override var referer: String,
    override var quality: Int,
    override var headers: Map<String, String> = mapOf(),
    /** Used for getExtractorVerifierJob() */
    override var extractorData: String? = null,
    override var type: ExtractorLinkType,
    override var audioTracks: List<AudioFile> = emptyList(),
) : ExtractorLink(
    source = source,
    name = name,
    url = "",
    referer = referer,
    quality = quality,
    headers = headers,
    extractorData = extractorData,
    type = type,
    audioTracks = audioTracks
) {
    constructor(
        source: String,
        name: String,
        playlist: List<PlayListItem>,
        referer: String,
        quality: Int,
        isM3u8: Boolean = false,
        headers: Map<String, String> = mapOf(),
        extractorData: String? = null,
    ) : this(
        source = source,
        name = name,
        playlist = playlist,
        referer = referer,
        quality = quality,
        type = if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO,
        headers = headers,
        extractorData = extractorData,
    )
}

/** Metadata about the file type used for downloads and exoplayer hint,
 * if you respond with the wrong one the file will fail to download or be played */
enum class ExtractorLinkType {
    /** Single stream of bytes no matter the actual file type */
    VIDEO,

    /** Split into several .ts files, has support for encrypted m3u8s */
    M3U8,

    /** Like m3u8 but uses xml, currently no download support */
    DASH,

    /** No support at the moment */
    TORRENT,

    /** No support at the moment */
    MAGNET;

    // See https://www.iana.org/assignments/media-types/media-types.xhtml
    @JsonIgnore
    fun getMimeType(): String {
        return when (this) {
            VIDEO -> "video/mp4"
            M3U8 -> "application/x-mpegURL"
            DASH -> "application/dash+xml"
            TORRENT -> "application/x-bittorrent"
            MAGNET -> "application/x-bittorrent"
        }
    }
}

private fun inferTypeFromUrl(url: String): ExtractorLinkType {
    val path = try {
        Url(url).encodedPath.decodeURLPart()
    } catch (_: Throwable) {
        // don't log magnet links as errors
        null
    }
    return when {
        path?.endsWith(".m3u8") == true -> ExtractorLinkType.M3U8
        path?.endsWith(".mpd") == true -> ExtractorLinkType.DASH
        path?.endsWith(".torrent") == true -> ExtractorLinkType.TORRENT
        url.startsWith("magnet:") -> ExtractorLinkType.MAGNET
        else -> ExtractorLinkType.VIDEO
    }
}

val INFER_TYPE: ExtractorLinkType? = null

/**
 * [Uuid] for the ClearKey DRM scheme.
 *
 *
 * ClearKey is supported on Android devices running Android 5.0 (API Level 21) and up.
 */
@Prerelease
val CLEARKEY_DRM_UUID = Uuid.fromLongs(-0x1d8e62a7567a4c37L, 0x781AB030AF78D30EL)

/**
 * [Uuid] for the Widevine DRM scheme.
 *
 *
 * Widevine is supported on Android devices running Android 4.3 (API Level 18) and up.
 */
@Prerelease
val WIDEVINE_DRM_UUID = Uuid.fromLongs(-0x121074568629b532L, -0x5c37d8232ae2de13L)

/**
 * [Uuid] for the PlayReady DRM scheme.
 *
 *
 * PlayReady is supported on all AndroidTV devices. Note that most other Android devices do not
 * provide PlayReady support.
 */
@Prerelease
val PLAYREADY_DRM_UUID = Uuid.fromLongs(-0x65fb0f8667bfbd7aL, -0x546d19a41f77a06bL)

// Deprecate after next stable

// @Deprecated("Use CLEARKEY_DRM_UUID", ReplaceWith("CLEARKEY_DRM_UUID"), level = DeprecationLevel.WARNING)
val CLEARKEY_UUID = CLEARKEY_DRM_UUID.toJavaUuid()

// @Deprecated("Use WIDEVINE_DRM_UUID", ReplaceWith("WIDEVINE_DRM_UUID"), level = DeprecationLevel.WARNING)
val WIDEVINE_UUID = WIDEVINE_DRM_UUID.toJavaUuid()

// @Deprecated("Use PLAYREADY_DRM_UUID", ReplaceWith("PLAYREADY_DRM_UUID"), level = DeprecationLevel.WARNING)
val PLAYREADY_UUID = PLAYREADY_DRM_UUID.toJavaUuid()

suspend fun newExtractorLink(
    source: String,
    name: String,
    url: String,
    type: ExtractorLinkType? = null,
    initializer: suspend ExtractorLink.() -> Unit = { }
): ExtractorLink {

    @Suppress("DEPRECATION_ERROR")
    val builder =
        ExtractorLink(
            source = source,
            name = name,
            url = url,
            type = type ?: INFER_TYPE
        )

    builder.initializer()
    return builder
}

// Deprecate after next stable
/* @Deprecated(
    message = "Use Kotlin Uuid (kotlin.uuid.Uuid) instead of Java UUID.",
    level = DeprecationLevel.WARNING,
) */
suspend fun newDrmExtractorLink(
    source: String,
    name: String,
    url: String,
    type: ExtractorLinkType? = null,
    uuid: java.util.UUID,
    initializer: suspend DrmExtractorLink.() -> Unit = { }
): DrmExtractorLink {
    @Suppress("DEPRECATION_ERROR")
    val builder =
        DrmExtractorLink(
            source = source,
            name = name,
            url = url,
            uuid = uuid.toKotlinUuid(),
            type = type ?: INFER_TYPE
        )

    builder.initializer()
    return builder
}

@Prerelease
suspend fun newDrmExtractorLink(
    source: String,
    name: String,
    url: String,
    type: ExtractorLinkType? = null,
    uuid: Uuid,
    initializer: suspend DrmExtractorLink.() -> Unit = {},
): DrmExtractorLink {
    @Suppress("DEPRECATION_ERROR")
    val builder =
        DrmExtractorLink(
            source = source,
            name = name,
            url = url,
            uuid = uuid,
            type = type ?: INFER_TYPE
        )

    builder.initializer()
    return builder
}

/** Class holds extracted DRM media info to be passed to the player.
 * @property source Name of the media source, appears on player layout.
 * @property name Title of the media, appears on player layout.
 * @property url Url string of media file
 * @property referer Referer that will be used by network request.
 * @property quality Quality of the media file
 * @property headers Headers <String, String> map that will be used by network request.
 * @property extractorData Used for getExtractorVerifierJob()
 * @property type the type of the media, use [INFER_TYPE] if you want to auto infer the type from the url
 * @property kid  Base64 value of The KID element (Key Id) contains the identifier of the key associated with a license.
 * @property key Base64 value of Key to be used to decrypt the media file.
 * @property uuid Drm [Uuid] [WIDEVINE_DRM_UUID], [PLAYREADY_DRM_UUID], [CLEARKEY_DRM_UUID] (by default) .. etc
 * @property kty Key type "oct" (octet sequence) by default
 * @property keyRequestParameters Parameters that will used to request the key.
 * @see newDrmExtractorLink
 * */
@Suppress("DEPRECATION")
open class DrmExtractorLink private constructor(
    override val source: String,
    override val name: String,
    override val url: String,
    override var referer: String,
    override var quality: Int,
    override var headers: Map<String, String> = mapOf(),
    /** Used for getExtractorVerifierJob() */
    override var extractorData: String? = null,
    override var type: ExtractorLinkType,
    open var kid: String? = null,
    open var key: String? = null,
    open var uuid: Uuid,
    open var kty: String? = null,
    open var keyRequestParameters: HashMap<String, String>,
    open var licenseUrl: String? = null,
    override var audioTracks: List<AudioFile> = emptyList(),
) : ExtractorLink(
    source, name, url, referer, quality, headers, extractorData, type, audioTracks
) {
    @Deprecated("Use newDrmExtractorLink", level = DeprecationLevel.ERROR)
    constructor(
        source: String,
        name: String,
        url: String,
        referer: String? = null,
        quality: Int? = null,
        /** the type of the media, use INFER_TYPE if you want to auto infer the type from the url */
        type: ExtractorLinkType? = INFER_TYPE,
        headers: Map<String, String> = mapOf(),
        /** Used for getExtractorVerifierJob() */
        extractorData: String? = null,
        kid: String? = null,
        key: String? = null,
        uuid: Uuid = CLEARKEY_DRM_UUID,
        kty: String? = "oct",
        keyRequestParameters: HashMap<String, String> = hashMapOf(),
        licenseUrl: String? = null,
    ) : this(
        source = source,
        name = name,
        url = url,
        referer = referer ?: "",
        quality = quality ?: Qualities.Unknown.value,
        headers = headers,
        extractorData = extractorData,
        type = type ?: inferTypeFromUrl(url),
        kid = kid,
        key = key,
        uuid = uuid,
        keyRequestParameters = keyRequestParameters,
        kty = kty,
        licenseUrl = licenseUrl,
    )

    @Deprecated("Use newDrmExtractorLink", level = DeprecationLevel.ERROR)
    constructor(
        source: String,
        name: String,
        url: String,
        referer: String,
        quality: Int,
        /** the type of the media, use INFER_TYPE if you want to auto infer the type from the url */
        type: ExtractorLinkType?,
        headers: Map<String, String> = mapOf(),
        /** Used for getExtractorVerifierJob() */
        extractorData: String? = null,
        kid: String? = null,
        key: String? = null,
        uuid: Uuid = CLEARKEY_DRM_UUID,
        kty: String? = "oct",
        keyRequestParameters: HashMap<String, String> = hashMapOf(),
        licenseUrl: String? = null,
    ) : this(
        source = source,
        name = name,
        url = url,
        referer = referer,
        quality = quality,
        headers = headers,
        extractorData = extractorData,
        type = type ?: inferTypeFromUrl(url),
        kid = kid,
        key = key,
        uuid = uuid,
        keyRequestParameters = keyRequestParameters,
        kty = kty,
        licenseUrl = licenseUrl,
    )

    @Deprecated(message = "Use Kotlin Uuid", level = DeprecationLevel.HIDDEN)
    fun setUuid(uuid: java.util.UUID) {
        this.uuid = uuid.toKotlinUuid()
    }

    @Deprecated(message = "Use Kotlin Uuid", level = DeprecationLevel.HIDDEN)
    fun getUuid(): java.util.UUID = this.uuid.toJavaUuid()
}

/** Class holds extracted media info to be passed to the player.
 * @property source Name of the media source, appears on player layout.
 * @property name Title of the media, appears on player layout.
 * @property url Url string of media file
 * @property referer Referer that will be used by network request.
 * @property quality Quality of the media file
 * @property headers Headers <String, String> map that will be used by network request.
 * @property extractorData Used for getExtractorVerifierJob()
 * @property type Extracted link type (Video, M3u8, Dash, Torrent or Magnet)
 * @property audioTracks List of separate audio tracks that can be used with this video
 * @see newExtractorLink
 * */
@Serializable
open class ExtractorLink
@Deprecated("Use newExtractorLink", level = DeprecationLevel.WARNING)
constructor(
    @SerialName("source") open val source: String,
    @SerialName("name") open val name: String,
    @SerialName("url") override val url: String,
    @SerialName("referer") override var referer: String,
    @SerialName("quality") open var quality: Int,
    @SerialName("headers") override var headers: Map<String, String> = mapOf(),
    /** Used for getExtractorVerifierJob() */
    @SerialName("extractorData") open var extractorData: String? = null,
    @SerialName("type") open var type: ExtractorLinkType,
    /** List of separate audio tracks that can be merged with this video */
    @SerialName("audioTracks") open var audioTracks: List<AudioFile> = emptyList(),
) : IDownloadableMinimum {
    @get:JsonIgnore val isM3u8: Boolean get() = type == ExtractorLinkType.M3U8
    @get:JsonIgnore val isDash: Boolean get() = type == ExtractorLinkType.DASH

    // Cached video size
    @Transient private var videoSize: Long? = null

    /**
     * Get video size in bytes with one head request. Only available for ExtractorLinkType.Video
     * @param timeoutSeconds timeout of the head request.
     */
    suspend fun getVideoSize(timeoutSeconds: Long = 3L): Long? {
        // Content-Length is not applicable to other types of formats
        if (this.type != ExtractorLinkType.VIDEO) return null

        videoSize = videoSize ?: runCatching {
            val response =
                app.head(this.url, headers = headers, referer = referer, timeout = timeoutSeconds)
            response.headers["Content-Length"]?.toLong()
        }.getOrNull()

        return videoSize
    }

    @JsonIgnore
    fun getAllHeaders(): Map<String, String> {
        if (referer.isBlank()) {
            return headers
        } else if (headers.keys.none { it.equals("referer", ignoreCase = true) }) {
            return headers + mapOf("referer" to referer)
        }
        return headers
    }

    @Suppress("DEPRECATION")
    @Deprecated("Use newExtractorLink", level = DeprecationLevel.ERROR)
    constructor(
        source: String,
        name: String,
        url: String,
        referer: String? = null,
        quality: Int? = null,
        /** the type of the media, use INFER_TYPE if you want to auto infer the type from the url */
        type: ExtractorLinkType? = INFER_TYPE,
        headers: Map<String, String> = mapOf(),
        /** Used for getExtractorVerifierJob() */
        extractorData: String? = null,
    ) : this(
        source = source,
        name = name,
        url = url,
        referer = referer ?: "",
        quality = quality ?: Qualities.Unknown.value,
        headers = headers,
        extractorData = extractorData,
        type = type ?: inferTypeFromUrl(url)
    )

    @Suppress("DEPRECATION")
    @Deprecated("Use newExtractorLink", level = DeprecationLevel.ERROR)
    constructor(
        source: String,
        name: String,
        url: String,
        referer: String,
        quality: Int,
        /** the type of the media, use INFER_TYPE if you want to auto infer the type from the url */
        type: ExtractorLinkType?,
        headers: Map<String, String> = mapOf(),
        /** Used for getExtractorVerifierJob() */
        extractorData: String? = null,
    ) : this(
        source = source,
        name = name,
        url = url,
        referer = referer,
        quality = quality,
        headers = headers,
        extractorData = extractorData,
        type = type ?: inferTypeFromUrl(url)
    )

    /**
     * Old constructor without isDash, allows for backwards compatibility with extensions.
     * Should be removed after all extensions have updated their cloudstream.jar
     **/
    @Suppress("DEPRECATION_ERROR")
    @Deprecated("Use newExtractorLink", level = DeprecationLevel.ERROR)
    constructor(
        source: String,
        name: String,
        url: String,
        referer: String,
        quality: Int,
        isM3u8: Boolean = false,
        headers: Map<String, String> = mapOf(),
        /** Used for getExtractorVerifierJob() */
        extractorData: String? = null
    ) : this(source, name, url, referer, quality, isM3u8, headers, extractorData, false)

    @Suppress("DEPRECATION")
    @Deprecated("Use newExtractorLink", level = DeprecationLevel.ERROR)
    constructor(
        source: String,
        name: String,
        url: String,
        referer: String,
        quality: Int,
        isM3u8: Boolean = false,
        headers: Map<String, String> = mapOf(),
        /** Used for getExtractorVerifierJob() */
        extractorData: String? = null,
        isDash: Boolean,
    ) : this(
        source = source,
        name = name,
        url = url,
        referer = referer,
        quality = quality,
        headers = headers,
        extractorData = extractorData,
        type = if (isDash) ExtractorLinkType.DASH else if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
    )

    override fun toString(): String {
        return "ExtractorLink(name=$name, url=$url, referer=$referer, type=$type)"
    }
}

/**
 * Removes https:// and www.
 * To match urls regardless of schema, perhaps Url() can be used?
 */
val schemaStripRegex = Regex("""^(https:|)//(www\.|)""")

enum class Qualities(var value: Int, val defaultPriority: Int) {
    Unknown(400, 4),
    P144(144, 0), // 144p
    P240(240, 2), // 240p
    P360(360, 3), // 360p
    P480(480, 4), // 480p
    P720(720, 5), // 720p
    P1080(1080, 6), // 1080p
    P1440(1440, 7), // 1440p
    P2160(2160, 8); // 4k or 2160p

    companion object {
        fun getStringByInt(qual: Int?): String {
            return when (qual) {
                0 -> "Auto"
                Unknown.value -> ""
                P2160.value -> "4K"
                null -> ""
                else -> "${qual}p"
            }
        }

        fun getStringByIntFull(quality: Int): String {
            return when (quality) {
                0 -> "Auto"
                Unknown.value -> "Unknown"
                P2160.value -> "4K"
                else -> "${quality}p"
            }
        }
    }
}

fun getQualityFromName(qualityName: String?): Int {
    if (qualityName == null)
        return Qualities.Unknown.value

    val match = qualityName.lowercase().replace("p", "").trim()
    return when (match) {
        "4k" -> Qualities.P2160
        else -> null
    }?.value ?: match.toIntOrNull() ?: Qualities.Unknown.value
}

private val packedRegex = Regex("""eval\(function\(p,a,c,k,e,.*\)\)""")
fun getPacked(string: String): String? {
    return packedRegex.find(string)?.value
}

fun getAndUnpack(string: String): String {
    val packedText = getPacked(string)
    return JsUnpacker(packedText).unpack() ?: string
}

suspend fun unshortenLinkSafe(url: String): String {
    return try {
        if (ShortLink.isShortLink(url))
            ShortLink.unshorten(url)
        else url
    } catch (e: Exception) {
        logError(e)
        url
    }
}

suspend fun loadExtractor(
    url: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    return loadExtractor(
        url = url,
        referer = null,
        subtitleCallback = subtitleCallback,
        callback = callback
    )
}


/**
 * Tries to load the appropriate extractor based on link, returns true if any extractor is loaded.
 * */
@Throws(CancellationException::class)
suspend fun loadExtractor(
    url: String,
    referer: String? = null,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    // Ensure this coroutine has not timed out
    coroutineScope { ensureActive() }

    val currentUrl = unshortenLinkSafe(url)
    val compareUrl = currentUrl.lowercase().replace(schemaStripRegex, "")

    // Iterate in reverse order so the new registered ExtractorApi takes priority
    for (index in extractorApis.lastIndex downTo 0) {
        val extractor = extractorApis[index]
        if (compareUrl.startsWith(extractor.mainUrl.replace(schemaStripRegex, ""))) {
            try {
                extractor.getUrl(currentUrl, referer, subtitleCallback, callback)
            } catch (e: Exception) {
                logError(e)
                // Rethrow if we have timed out
                if (e is CancellationException) {
                    throw e
                }
            }
            return true
        }
    }

    // this is to match mirror domains - like example.com, example.net
    for (index in extractorApis.lastIndex downTo 0) {
        val extractor = extractorApis[index]
        if (Levenshtein.partialRatio(
                extractor.mainUrl,
                currentUrl
            ) > 80
        ) {
            try {
                extractor.getUrl(currentUrl, referer, subtitleCallback, callback)
            } catch (e: Exception) {
                logError(e)
                // Rethrow if we have timed out
                if (e is CancellationException) {
                    throw e
                }
            }
            return true
        }
    }

    return false
}

val extractorApis: AtomicMutableList<ExtractorApi> = atomicListOf(
    //AllProvider(),
    Mp4Upload(),
    StreamTape(),
    StreamTapeNet(),
    ShaveTape(),
    StreamTapeXyz(),
    Watchadsontape(),

    //mixdrop extractors
    MixDropBz(),
    MixDropCh(),
    MixDropTo(),
    MixDropAg(),
    MixDrop(),
    MixDropPs(),
    Mdy(),
    MxDropTo(),
    MixDropSi(),

    XStreamCdn(),

    StreamSB(),
    Sblona(),
    Vidgomunimesb(),
    StreamSilk(),
    StreamSB1(),
    StreamSB2(),
    StreamSB3(),
    StreamSB4(),
    StreamSB5(),
    StreamSB6(),
    StreamSB7(),
    StreamSB8(),
    StreamSB9(),
    StreamSB10(),
    StreamSB11(),
    SBfull(),
    // Streamhub(), cause Streamhub2() works
    Streamhub2(),
    Ssbstream(),
    Sbthe(),
    Vidgomunime(),
    Sbflix(),
    Streamsss(),
    Sbspeed(),
    Sbsonic(),
    Sbface(),
    Sbrapid(),
    Lvturbo(),

    Fastream(),
    Videa(),
    FEmbed(),
    FeHD(),
    Fplayer(),
    DBfilm(),
    Luxubu(),
    LayarKaca(),
    Rasacintaku(),
    FEnet(),
    Kotakajair(),
    Cdnplayer(),
    //  WatchSB(), 'cause StreamSB.kt works
    Uqload(),
    Uqload1(),
    Uqload2(),
    Uqloadcx(),
    Uqloadbz(),
    Evoload(),
    Evoload1(),
    UpstreamExtractor(),

    Odnoklassniki(),
    TauVideo(),
    SibNet(),
    ContentX(),
    Hotlinger(),
    FourCX(),
    PlayRu(),
    FourPlayRu(),
    Pichive(),
    FourPichive(),
    HDMomPlayer(),
    HDPlayerSystem(),
    VideoSeyred(),
    PeaceMakerst(),
    HDStreamAble(),
    RapidVid(),
    TRsTX(),
    VidMoxy(),
    Sobreatsesuyp(),
    PixelDrain(),
    PixelDrainDev(),
    MailRu(),

    OkRuSSL(),
    OkRuSSLMobile(),
    OkRuHTTP(),
    OkRuHTTPMobile(),
    Sendvid(),

    // dood extractors
    DoodCxExtractor(),
    DoodPmExtractor(),
    DoodToExtractor(),
    DoodSoExtractor(),
    DoodLaExtractor(),
    Dooood(),
    D0000d(),
    D000dCom(),
    DoodstreamCom(),
    DoodWsExtractor(),
    DoodShExtractor(),
    DoodWatchExtractor(),
    DoodWfExtractor(),
    DoodYtExtractor(),
    Doodspro(),
    Dsvplay(),

    // GenericM3U8(),
    Zplayer(),
    ZplayerV2(),
    Upstream(),

    Maxstream(),
    Tantifilm(),
    Userload(),
    Supervideo(),
    Streamcash(),

    // StreamSB.kt works
    //  SBPlay(),
    //  SBPlay1(),
    //  SBPlay2(),

    PlayerVoxzer(),

    Blogger(),
    YourUpload(),

    Hxfile(),
    KotakAnimeid(),
    Neonime8n(),
    Neonime7n(),
    Yufiles(),
    Aico(),

    JWPlayer(),
    Meownime(),
    DesuArcg(),
    DesuOdchan(),
    DesuOdvip(),
    DesuDrive(),


    Keephealth(),
    Sbnet(),
    Sbasian(),
    Sblongvu(),
    Fembed9hd(),
    StreamM4u(),
    Krakenfiles(),
    Gofile(),
    Vicloud(),
    Uservideo(),
    Userscloud(),
    HubuCloud(),

    Movhide(),
    StreamhideCom(),
    StreamhideTo(),
    Wibufile(),
    FileMoonIn(),
    Moviesm4u(),
    Filesim(),
    Multimoviesshg(),
    Ahvsh(),
    Guccihide(),
    FileMoon(),
    FileMoonSx(),
    FilemoonV2(),

    Vido(),
    Linkbox(),
    Acefile(),
    Embedgram(),
    Mvidoo(),
    Streamplay(),
    Vidmoly(),
    Vidmolyme(),
    Vidmolyto(),
    Vidmolybiz(),
    Voe(),
    Voe1(),
    Voe2(),
    Tubeless(),
    Moviehab(),
    MoviehabNet(),
    Jeniusplay(),
    StreamoUpload(),
    Vidara(),
    Vidavaca(),
    Vidaraa(),
    Vidaraw(),
    Vidarax(),
    VidaraSo(),
    Vidaratem(),
    VidaaraxCom(),
    VidaaraxNet(),
    Odysseusa(),
    Handfacesnap(),
    Namefacesnap(),
    Thebesthostertv(),
    Vidmatrixa(),
    VidChampions(),
    Antarcticadocs(),
    Nameitweb(),

    GamoVideo(),
    Gdriveplayerapi(),
    Gdriveplayerapp(),
    Gdriveplayerfun(),
    Gdriveplayerio(),
    Gdriveplayerme(),
    Gdriveplayerbiz(),
    Gdriveplayerorg(),
    Gdriveplayerus(),
    Gdriveplayerco(),
    GoodstreamExtractor(),
    Gdriveplayer(),
    DatabaseGdrive(),
    DatabaseGdrive2(),
    Mediafire(),

    YoutubeExtractor(),
    YoutubeShortLinkExtractor(),
    YoutubeMobileExtractor(),
    YoutubeNoCookieExtractor(),
    Streamlare(),
    PlayLtXyz(),

    Cda(),
    Dailymotion(),
    Ztreamhub(),
    Rabbitstream(),
    Dokicloud(),
    Megacloud(),
    VidhideExtractor(),
    VidHidePro(),
    VidHidePro1(),
    VidHidePro2(),
    VidHidePro3(),
    VidHidePro4(),
    VidHidePro5(),
    VidHidePro6(),
    VidHideHub(),
    Ryderjet(),
    VidNest(),
    Dhtpre(),

    // CineMM Redirects
    Dhcplay(),
    HglinkTo(),

    // CineMM mirrors
    HgplayCDN(),
    Habetar(),
    Yuguaab(),
    Guxhag(),
    Auvexiug(),
    Xenolyzb(),
    Haxloppd(),
    Cavanhabg(),
    Dumbalag(),
    Uasopt(),

    Smoothpre(),
    Peytonepre(),
    LuluStream(),
    Lulustream1(),
    Lulustream2(),
    Luluvdoo(),
    StreamWishExtractor(),
    StreamHLS(),
    BigwarpIO(),
    BigwarpArt(),
    BgwpCC(),
    WishembedPro(),
    CdnwishCom(),
    FlaswishCom(),
    SfastwishCom(),
    Playerwish(),
    StreamEmbed(),
    EmturbovidExtractor(),
    Vtbe(),
    SecvideoOnline(),
    FsstOnline(),
    CsstOnline(),
    DsstOnline(),
    Simpulumlamerop(),
    Urochsunloath(),
    NathanFromSubject(),
    Yipsu(),
    MetaGnathTuggers(),
    Geodailymotion(),
    Mwish(),
    Hgcloudto(),
    Dwish(),
    Ewish(),
    Kswplayer(),
    Wishfast(),
    Streamwish2(),
    Strwish(),
    Strwish2(),
    Awish(),
    Obeywish(),
    Jodwish(),
    Swhoi(),
    Multimovies(),
    UqloadsXyz(),
    Doodporn(),
    Asnwish(),
    Nekowish(),
    Nekostream(),
    Swdyu(),
    Wishonly(),
    Ds2play(),
    Ds2video(),
    Vidsonic(),
    Vixeo(),
    InternetArchive(),
    VidStack(),
    GDMirrorbot(),
    Techinmind(),
    Server1uns(),
    VinovoSi(),
    VinovoTo(),
    Vidoza(),
    Videzz(),
    CloudMailRu(),
    HubCloud(),
    VkExtractor(),
    Bysezejataos(),
    ByseSX(),
    ByseVepoin(),
    ByseBuho(),
    MyVidPlay(),
    Playmogo(),
    Vide0Net(),
    Up4Stream(),
    Up4FunTop(),
    GUpload(),
    HlsWish(),
    ByseQekaho(),
    Flyfile(),
    Firestream(),
    FirestreamSite(),
    Vids(),
    Playmate(),
    Hexload(),
)


fun getExtractorApiFromName(name: String): ExtractorApi {
    for (api in extractorApis) {
        if (api.name == name) return api
    }
    return extractorApis[0]
}

fun requireReferer(name: String): Boolean {
    return getExtractorApiFromName(name).requiresReferer
}

fun httpsify(url: String): String {
    return if (url.startsWith("//")) "https:$url" else url
}

suspend fun getPostForm(requestUrl: String, html: String): String? {
    val document = Ksoup.parse(html)
    val inputs = document.select("Form > input")
    if (inputs.size < 4) return null
    var op: String? = null
    var id: String? = null
    var mode: String? = null
    var hash: String? = null

    for (input in inputs) {
        val value = input.attr("value")
        when (input.attr("name")) {
            "op" -> op = value
            "id" -> id = value
            "mode" -> mode = value
            "hash" -> hash = value
            else -> Unit
        }
    }
    if (op == null || id == null || mode == null || hash == null) {
        return null
    }
    delay(5000) // ye this is needed, wont work with 0 delay

    return app.post(
        requestUrl,
        headers = mapOf(
            "content-type" to "application/x-www-form-urlencoded",
            "referer" to requestUrl,
            "user-agent" to USER_AGENT,
            "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
        ),
        data = mapOf("op" to op, "id" to id, "mode" to mode, "hash" to hash)
    ).text
}

fun ExtractorApi.fixUrl(url: String): String {
    if (url.startsWith("http") ||
        // Do not fix JSON objects when passed as urls.
        url.startsWith("{\"")
    ) {
        return url
    }
    if (url.isEmpty()) {
        return ""
    }

    val startsWithNoHttp = url.startsWith("//")
    if (startsWithNoHttp) {
        return "https:$url"
    } else {
        if (url.startsWith('/')) {
            return mainUrl + url
        }
        return "$mainUrl/$url"
    }
}

abstract class ExtractorApi {
    abstract val name: String
    abstract val mainUrl: String
    abstract val requiresReferer: Boolean

    /** Determines which plugin a given provider is from. This is the full path to the plugin. */
    var sourcePlugin: String? = null

    //suspend fun getSafeUrl(url: String, referer: String? = null): List<ExtractorLink>? {
    //    return safeAsync { getUrl(url, referer) }
    //}

    // this is the new extractorapi, override to add subtitles and stuff
    @Throws
    open suspend fun getUrl(
        url: String,
        referer: String? = null,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        getUrl(url, referer)?.forEach(callback)
    }

    suspend fun getSafeUrl(
        url: String,
        referer: String? = null,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            getUrl(url, referer, subtitleCallback, callback)
        } catch (e: Exception) {
            logError(e)
        }
    }

    /**
     * Will throw errors, use getSafeUrl if you don't want to handle the exception yourself
     */
    @Throws
    open suspend fun getUrl(url: String, referer: String? = null): List<ExtractorLink>? {
        return emptyList()
    }

    open fun getExtractorUrl(id: String): String {
        return id
    }
}
