package com.xorko.backlogmanager.game.data.dto.shared

import kotlinx.serialization.Serializable

@Serializable
data class SharedOnlineRatingDto(
    val value: Double,
    val count: Int,
)
