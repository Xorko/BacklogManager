package com.xorko.backlogmanager.game.domain.models

import kotlinx.datetime.LocalDate

data class Game(
    val id: Long,
    val name: String,
    val description: String,
    val released: LocalDate?,
    val coverUrl: String?,
    val artworksUrls: List<String>,
    val platforms: List<Platform>,
    val developers: List<Developer>,
    val publishers: List<Publisher>,
    val criticsRating: OnlineRating?,
    val usersRating: OnlineRating?,
)
