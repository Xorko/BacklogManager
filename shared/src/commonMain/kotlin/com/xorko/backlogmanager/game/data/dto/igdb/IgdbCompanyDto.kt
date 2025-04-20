package com.xorko.backlogmanager.game.data.dto.igdb

import kotlinx.serialization.Serializable

@Serializable
data class IgdbCompanyDto(
    val id: Long,
    val name: String,
)