package com.xorko.backlogmanager.app

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object GameGraph : Route

    @Serializable
    data object GameList : Route

    @Serializable
    data class GameDetail(
        val gameId: Long,
    ) : Route
}
