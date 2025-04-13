package com.xorko.backlogmanager.game.domain

import kotlinx.datetime.LocalDate

data class Game(
    val id: Long,
    val name: String,
    val description: String,
    val released: LocalDate,
    val coverUrl: String,
    val developers: List<Developer>,
    val publishers: List<Publisher>,
    val rating: Int,
    val ratingsCount: Int,
)
