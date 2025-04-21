package com.xorko.backlogmanager.game.data.network

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.data.dto.igdb.IgdbGameDto
import com.xorko.backlogmanager.game.data.dto.shared.SharedGameDto

interface RemoteGameDataSource {
    suspend fun searchGames(
        query: String,
        resultLimit: Int? = null,
    ): Either<DataError.Remote, List<IgdbGameDto>>

    suspend fun getGames(
        resultLimit: Int? = null,
        resultOffset: Int? = null,
    ): Either<DataError.Remote, List<SharedGameDto>>

    suspend fun getPopularGames(
        resultLimit: Int? = null,
        resultOffset: Int? = null,
    ): Either<DataError.Remote, List<SharedGameDto>>

    suspend fun getGameById(id: Long): Either<DataError.Remote, IgdbGameDto?>
}
