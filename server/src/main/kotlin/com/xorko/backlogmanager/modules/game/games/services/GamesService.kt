package com.xorko.backlogmanager.modules.game.games.services

import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.modules.game.games.repositories.GamesRepository

private const val IGDB_GAMES_ROUTE = "games"

class GamesService(
    private val gamesRepository: GamesRepository,
) {
    suspend fun getGames(
        limit: Int,
        offset: Int,
    ): List<Game> =
        gamesRepository.getGames(
            limit = limit,
            offset = offset,
        )

    suspend fun getGamesByIds(ids: List<Long>): List<Game> = gamesRepository.getGamesById(ids = ids)
}
