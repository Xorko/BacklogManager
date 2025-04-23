package com.xorko.backlogmanager.modules.game.games.repositories

import com.xorko.backlogmanager.game.domain.models.Game

interface GamesRepository {
    suspend fun getGames(
        offset: Int,
        limit: Int,
    ): List<Game>

    suspend fun getGamesById(ids: List<Long>): List<Game>
}
