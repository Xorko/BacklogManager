package com.xorko.backlogmanager.game.data.dto.shared

import kotlinx.serialization.Serializable

@Serializable
data class SharedPublisherDto(
    val id: Long,
    val name: String,
)
