package com.xorko.backlogmanager.game.data.network

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.data.network.dto.BackloggedGameDto

interface RemoteBacklogDataSource {
    suspend fun getBackloggedGames(userId: String): Either<DataError.Remote, List<BackloggedGameDto>>

    suspend fun upsertBackloggedGame(dto: BackloggedGameDto): Either<DataError.Remote, Unit>

    suspend fun deleteBackloggedGame(gameId: Long, userId: String): Either<DataError.Remote, Unit>
}
