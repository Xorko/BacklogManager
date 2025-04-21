package com.xorko.backlogmanager.game.data.dto.igdb.mappers

import com.xorko.backlogmanager.game.data.dto.igdb.IgdbGameDto
import com.xorko.backlogmanager.game.data.dto.igdb.IgdbInvolvedCompanyDto
import com.xorko.backlogmanager.game.data.dto.igdb.IgdbPlatformDto
import com.xorko.backlogmanager.game.domain.models.Company.Developer
import com.xorko.backlogmanager.game.domain.models.Company.Publisher
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.domain.models.OnlineRating
import com.xorko.backlogmanager.game.domain.models.Platform
import com.xorko.backlogmanager.utils.toLocalDate

fun IgdbGameDto.toGame() =
    Game(
        id = id,
        name = name,
        description = summary ?: "",
        initialReleaseDate = firstReleaseDate?.toLocalDate(),
        coverUrl = cover?.imageId?.toCoverUrl(),
        artworksUrls = artworks?.mapNotNull { it.imageId.toCoverUrl() } ?: emptyList(),
        developers = involvedCompanies?.mapNotNull { it.toDeveloperOrNull() } ?: emptyList(),
        publishers =
            involvedCompanies
                ?.filter { !it.porting && !it.supporting }
                ?.mapNotNull { it.toPublisherOrNull() } ?: emptyList(),
        criticsRating =
            makeOnlineRatingOrNull(
                rating = aggregatedRating,
                count = aggregatedRatingCount,
            ),
        usersRating =
            makeOnlineRatingOrNull(
                rating = rating,
                count = ratingCount,
            ),
        platforms = platforms?.map { it.toPlatform() } ?: emptyList(),
    )

fun IgdbPlatformDto.toPlatform() =
    Platform(
        id = id,
        name = name,
    )

fun IgdbInvolvedCompanyDto.toDeveloperOrNull(): Developer? {
    if (!developer) return null
    return Developer(
        id = company.id,
        name = company.name,
    )
}

fun IgdbInvolvedCompanyDto.toPublisherOrNull(): Publisher? {
    if (!publisher) return null
    return Publisher(
        id = company.id,
        name = company.name,
    )
}

private fun makeOnlineRatingOrNull(
    rating: Double?,
    count: Int?,
) = if (rating != null && count != null) {
    OnlineRating(
        value = rating,
        count = count,
    )
} else {
    null
}
