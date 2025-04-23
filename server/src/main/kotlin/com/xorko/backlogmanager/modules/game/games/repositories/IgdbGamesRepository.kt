package com.xorko.backlogmanager.modules.game.games.repositories

import com.xorko.backlogmanager.game.data.dto.igdb.mappers.toGame
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.modules.game.common.data.IgdbClient
import com.xorko.backlogmanager.modules.game.common.domain.GameModuleException.GameNotFoundException

class IgdbGamesRepository(
    private val igdbClient: IgdbClient,
) : GamesRepository {
    override suspend fun getGames(
        offset: Int,
        limit: Int,
    ): List<Game> = igdbClient.getGames(limit = limit, offset = offset).map { it.toGame() }

    override suspend fun getGamesById(ids: List<Long>): List<Game> {
        val games = igdbClient.getGamesById(ids)
        if (games.size != ids.size) {
            throw GameNotFoundException()
        }
        return games.map { it.toGame() }
    }
}
