package com.xorko.backlogmanager.game.domain.models

import com.xorko.backlogmanager.game.domain.models.Company.Developer
import com.xorko.backlogmanager.game.domain.models.Company.Publisher
import kotlinx.datetime.LocalDate

data class Game(
    val id: Long,
    val name: String,
    val description: String,
    val genres: List<Genre>,
    val initialReleaseDate: LocalDate?,
    val coverUrl: String?,
    val artworksUrls: List<String>,
    val platforms: List<Platform>,
    val developers: List<Developer>,
    val publishers: List<Publisher>,
    val criticsRating: OnlineRating?,
    val usersRating: OnlineRating?,
)
