package com.xorko.backlogmanager.game.data.dto.igdb

import kotlinx.serialization.Serializable

@Serializable
data class IgdbGenreDto(
    val id: Long,
    val name: String,
)
