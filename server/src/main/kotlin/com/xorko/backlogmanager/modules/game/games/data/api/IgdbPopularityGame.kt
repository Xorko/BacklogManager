package com.xorko.backlogmanager.modules.game.games.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IgdbPopularityGame(
    @SerialName("game_id") val id: Long,
    @SerialName("value") val popularityScore: Double,
)
