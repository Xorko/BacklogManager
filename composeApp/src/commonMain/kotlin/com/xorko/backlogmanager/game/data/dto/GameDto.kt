package com.xorko.backlogmanager.game.data.dto

import com.xorko.backlogmanager.game.data.dto.serializers.ImageIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Long,
    val name: String,
    val summary: String? = null,
    val cover: CoverDto? = null,
    val artworks: List<ArtworkDto>? = null,
    val platforms: List<PlatformDto>? = null,
    @SerialName("involved_companies") val involvedCompanies: List<InvolvedCompanyDto>? = null,
    @SerialName("first_release_date") val firstReleaseDate: Long? = null,
    val rating: Double? = null,
    @SerialName("rating_count") val ratingCount: Int? = null,
    @SerialName("aggregated_rating") val aggregatedRating: Double? = null,
    @SerialName("aggregated_rating_count") val aggregatedRatingCount: Int? = null,
)

@Serializable
data class CoverDto(
    val id: Long,
    @SerialName("image_id") @Serializable(with = ImageIdSerializer::class) val imageId: ImageId?,
)

@Serializable
data class ArtworkDto(
    val id: Long,
    @SerialName("image_id") @Serializable(with = ImageIdSerializer::class) val imageId: ImageId?,
)

@Serializable
data class ImageId(
    val value: String,
) {
    fun toCoverUrl(size: IgdbImageSize = IgdbImageSize.COVER_BIG): String = "https://images.igdb.com/igdb/image/upload/t_$size/$value.jpg"
}

@Serializable
data class PlatformDto(
    val id: Long,
    val name: String,
)

@Serializable
data class CompanyDto(
    val id: Long,
    val name: String,
)

@Serializable
data class InvolvedCompanyDto(
    val id: Long,
    val company: CompanyDto,
    val developer: Boolean,
    val publisher: Boolean,
)

enum class IgdbImageSize(
    val raw: String,
) {
    THUMB("thumb"),
    COVER_SMALL("cover_small"),
    COVER_BIG("cover_big"),
    LOGO_MED("logo_med"),
    SCREENSHOT_BIG("screenshot_big"),
    SCREENSHOT_HUGE("screenshot_huge"),
    MEDIUM("720p"),
    HD("1080p"),
    ;

    override fun toString(): String = raw
}
