package com.xorko.backlogmanager.game.data.mappers

import com.xorko.backlogmanager.game.data.dto.GameDto
import com.xorko.backlogmanager.game.data.dto.InvolvedCompanyDto
import com.xorko.backlogmanager.game.domain.models.Developer
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.domain.models.OnlineRating
import com.xorko.backlogmanager.game.domain.models.Platform
import com.xorko.backlogmanager.game.domain.models.Publisher
import com.xorko.backlogmanager.utils.toLocalDate

fun GameDto.toGame() =
    Game(
        id = id,
        name = name,
        description = summary ?: "",
        released = firstReleaseDate?.toLocalDate(),
        coverUrl = cover?.imageId?.toCoverUrl(),
        artworksUrls = artworks?.mapNotNull { it.imageId?.toCoverUrl() } ?: emptyList(),
        developers = involvedCompanies?.mapNotNull { it.toDeveloperOrNull() } ?: emptyList(),
        publishers = involvedCompanies?.mapNotNull { it.toPublisherOrNull() } ?: emptyList(),
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
        platforms = platforms?.map { Platform(it.id, it.name) } ?: emptyList(),
    )

fun InvolvedCompanyDto.toDeveloperOrNull(): Developer? {
    if (!developer) return null
    return Developer(
        id = company.id,
        name = company.name,
    )
}

fun InvolvedCompanyDto.toPublisherOrNull(): Publisher? {
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
