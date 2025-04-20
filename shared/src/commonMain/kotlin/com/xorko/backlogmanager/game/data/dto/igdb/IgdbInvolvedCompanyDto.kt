package com.xorko.backlogmanager.game.data.dto.igdb

import kotlinx.serialization.Serializable

@Serializable
data class IgdbInvolvedCompanyDto(
    val id: Long,
    val company: IgdbCompanyDto,
    val developer: Boolean,
    val publisher: Boolean,
)