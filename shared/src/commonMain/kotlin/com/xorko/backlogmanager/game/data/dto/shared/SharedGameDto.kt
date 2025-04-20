package com.xorko.backlogmanager.game.data.dto.shared

import kotlinx.serialization.Serializable

@Serializable
data class SharedGameDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val coverUrl: String? = null,
    val artworksUrls: List<String>? = null,
    val platforms: List<SharedPlatformDto>? = null,
    val developers: List<SharedDeveloperDto>? = null,
    val publishers: List<SharedPublisherDto>? = null,
    val initialReleaseDate: String? = null,
    val usersRating: SharedOnlineRatingDto? = null,
    val criticsRating: SharedOnlineRatingDto? = null,
)
