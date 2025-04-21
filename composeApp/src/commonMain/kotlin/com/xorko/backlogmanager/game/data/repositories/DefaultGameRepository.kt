package com.xorko.backlogmanager.game.data.repositories

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.data.dto.igdb.mappers.toGame
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toGame
import com.xorko.backlogmanager.game.data.network.RemoteGameDataSource
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.domain.models.Game

class DefaultGameRepository(
    private val remoteGameDataSource: RemoteGameDataSource,
) : GameRepository {
    override suspend fun searchGames(query: String): Either<DataError.Remote, List<Game>> =
        remoteGameDataSource.searchGames(query).map { games ->
            Logger.v("games: $games", tag = this::class.simpleName)
            games.map { it.toGame() }
        }

    override suspend fun getGames(
        offset: Int,
        limit: Int,
    ): Either<DataError.Remote, List<Game>> =
        remoteGameDataSource
            .getGames(
                resultLimit = limit,
                resultOffset = offset,
            ).map { games ->
                Logger.v("games: $games", tag = this::class.simpleName)
                games.map { it.toGame() }
            }

    override suspend fun getPopularGames(
        offset: Int,
        limit: Int,
    ): Either<DataError.Remote, List<Game>> =
        remoteGameDataSource
            .getPopularGames(
                resultLimit = limit,
                resultOffset = offset,
            ).map { games ->
                Logger.v("Popular games: $games", tag = this::class.simpleName)
                games.map { it.toGame() }
            }

    override suspend fun getGameById(id: Long): Either<DataError.Remote, Game?> = remoteGameDataSource.getGameById(id).map { it?.toGame() }
}
