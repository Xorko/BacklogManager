package com.xorko.backlogmanager.game.data.network

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.data.dto.GameDto

interface RemoteGameDataSource {
    suspend fun searchGames(
        query: String,
        resultLimit: Int? = null,
    ): Either<DataError.Remote, List<GameDto>>
}
