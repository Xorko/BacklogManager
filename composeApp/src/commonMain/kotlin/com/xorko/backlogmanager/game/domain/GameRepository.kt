package com.xorko.backlogmanager.game.domain

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.domain.models.Game

interface GameRepository {
    suspend fun searchGames(query: String): Either<DataError.Remote, List<Game>>

    suspend fun getGames(
        offset: Int = 0,
        limit: Int = 20,
    ): Either<DataError.Remote, List<Game>>

    suspend fun getPopularGames(
        offset: Int = 0,
        limit: Int = 20,
    ): Either<DataError.Remote, List<Game>>

    suspend fun getGameById(id: Long): Either<DataError.Remote, Game?>
}
