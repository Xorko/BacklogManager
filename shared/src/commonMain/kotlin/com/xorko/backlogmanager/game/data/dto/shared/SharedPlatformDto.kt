package com.xorko.backlogmanager.game.data.dto.shared

import kotlinx.serialization.Serializable

@Serializable
data class SharedPlatformDto(
    val id: Long,
    val name: String,
)
