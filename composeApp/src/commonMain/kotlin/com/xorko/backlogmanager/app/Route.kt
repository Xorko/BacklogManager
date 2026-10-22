package com.xorko.backlogmanager.app

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object GameList : Route

    @Serializable
    data class GameDetail(
        val gameId: Long,
    ) : Route
}
