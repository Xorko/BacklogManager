package com.xorko.backlogmanager.game.data.dto.igdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IgdbGameDto(
    val id: Long,
    val name: String,
    val summary: String? = null,
    val cover: IgdbCoverDto? = null,
    val artworks: List<IgdbArtworkDto>? = null,
    val platforms: List<IgdbPlatformDto>? = null,
    @SerialName("involved_companies") val involvedCompanies: List<IgdbInvolvedCompanyDto>? = null,
    @SerialName("first_release_date") val firstReleaseDate: Long? = null,
    val rating: Double? = null,
    @SerialName("rating_count") val ratingCount: Int? = null,
    @SerialName("aggregated_rating") val aggregatedRating: Double? = null,
    @SerialName("aggregated_rating_count") val aggregatedRatingCount: Int? = null,
)
