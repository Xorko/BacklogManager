package com.xorko.backlogmanager.game.data.repositories

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.data.mappers.toGame
import com.xorko.backlogmanager.game.data.network.RemoteGameDataSource
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.domain.models.Game

class DefaultGameRepository(
    private val remoteGameDataSource: RemoteGameDataSource,
) : GameRepository {
    override suspend fun searchGames(query: String): Either<DataError.Remote, List<Game>> =
        remoteGameDataSource.searchGames(query).map { games ->
            println("games: $games")
            games.map { it.toGame() }
        }
}
