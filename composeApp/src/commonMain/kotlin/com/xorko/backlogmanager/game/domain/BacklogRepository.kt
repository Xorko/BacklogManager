package com.xorko.backlogmanager.game.domain

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.Game
import kotlinx.coroutines.flow.Flow

interface BacklogRepository {
    fun getBackloggedGames(): Flow<List<BackloggedGame>>

    fun getBackloggedGameById(gameId: Long): Flow<BackloggedGame?>

    fun isGameInBacklog(gameId: Long): Flow<Boolean>

    suspend fun addToBacklog(game: Game): Either<DataError, Unit>

    suspend fun removeFromBacklog(gameId: Long): Either<DataError, Unit>

    suspend fun updateBackloggedGameStatus(backloggedGame: BackloggedGame): Either<DataError, Unit>

    suspend fun sync(): Either<DataError, Unit>
}
