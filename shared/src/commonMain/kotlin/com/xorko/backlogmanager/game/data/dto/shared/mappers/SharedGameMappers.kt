package com.xorko.backlogmanager.game.data.dto.shared.mappers

import com.xorko.backlogmanager.game.data.dto.shared.SharedDeveloperDto
import com.xorko.backlogmanager.game.data.dto.shared.SharedGameDto
import com.xorko.backlogmanager.game.data.dto.shared.SharedOnlineRatingDto
import com.xorko.backlogmanager.game.data.dto.shared.SharedPlatformDto
import com.xorko.backlogmanager.game.data.dto.shared.SharedPublisherDto
import com.xorko.backlogmanager.game.domain.models.Company.Developer
import com.xorko.backlogmanager.game.domain.models.Company.Publisher
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.domain.models.OnlineRating
import com.xorko.backlogmanager.game.domain.models.Platform
import kotlinx.datetime.LocalDate

// region SharedDto to Domain
fun SharedGameDto.toGame() =
    Game(
        id = id,
        name = name,
        description = description ?: "",
        initialReleaseDate = initialReleaseDate?.toLocalDateOrNull(),
        coverUrl = coverUrl,
        artworksUrls = artworksUrls ?: emptyList(),
        platforms = platforms?.map { it.toPlatform() } ?: emptyList(),
        developers = developers?.map { it.toDeveloper() } ?: emptyList(),
        publishers = publishers?.map { it.toPublisher() } ?: emptyList(),
        criticsRating = criticsRating?.toOnlineRating(),
        usersRating = usersRating?.toOnlineRating(),
    )

fun SharedPlatformDto.toPlatform() =
    Platform(
        id = id,
        name = name,
    )

fun SharedDeveloperDto.toDeveloper() =
    Developer(
        id = id,
        name = name,
    )

fun SharedPublisherDto.toPublisher() =
    Publisher(
        id = id,
        name = name,
    )

fun SharedOnlineRatingDto.toOnlineRating() =
    OnlineRating(
        value = value,
        count = count,
    )

private fun String.toLocalDateOrNull() =
    try {
        LocalDate.parse(this)
    } catch (_: Exception) {
        null
    }
// endregion

// region Domain to SharedDto
fun Game.toSharedGameDto() =
    SharedGameDto(
        id = id,
        name = name,
        description = description,
        coverUrl = coverUrl,
        artworksUrls = artworksUrls,
        platforms = platforms.map { it.toSharedPlatformDto() },
        developers = developers.map { it.toSharedDeveloperDto() },
        publishers = publishers.map { it.toSharedPublisherDto() },
        initialReleaseDate = initialReleaseDate.toString(),
        usersRating = usersRating?.toSharedOnlineRatingDto(),
        criticsRating = criticsRating?.toSharedOnlineRatingDto(),
    )

fun Platform.toSharedPlatformDto() =
    SharedPlatformDto(
        id = id,
        name = name,
    )

fun Developer.toSharedDeveloperDto() =
    SharedDeveloperDto(
        id = id,
        name = name,
    )

fun Publisher.toSharedPublisherDto() =
    SharedPublisherDto(
        id = id,
        name = name,
    )

fun OnlineRating.toSharedOnlineRatingDto() =
    SharedOnlineRatingDto(
        value = value,
        count = count,
    )
// endregion
