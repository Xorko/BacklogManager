package com.xorko.backlogmanager.game.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackloggedGameDto(
    val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("game_id") val gameId: Long,
    val status: String,
    @SerialName("backlogged_date") val backloggedDate: String,
    @SerialName("started_date") val startedDate: String? = null,
    @SerialName("finished_date") val finishedDate: String? = null,
    @SerialName("game_data") val gameData: String? = null,
    @SerialName("updated_at") val updatedAt: Long? = null,
)
