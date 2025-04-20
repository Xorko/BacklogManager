package com.xorko.backlogmanager.igdb.games.services

import com.xorko.backlogmanager.core.domain.exceptions.RemoteException
import com.xorko.backlogmanager.core.domain.exceptions.ServerException
import com.xorko.backlogmanager.core.domain.exceptions.UnauthorizedException
import com.xorko.backlogmanager.game.data.dto.igdb.IgdbGameDto
import com.xorko.backlogmanager.game.data.utils.IgdbQueryFactory
import com.xorko.backlogmanager.igdbHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

private const val IGDB_GAMES_ROUTE = "games"

class IgdbGameService {
    suspend fun getGames(
        limit: Int,
        offset: Int,
    ): List<IgdbGameDto> {
        val response =
            try {
                igdbHttpClient.post(IGDB_GAMES_ROUTE) {
                    setBody(
                        IgdbQueryFactory.getGames(
                            limit = limit,
                            offset = offset,
                        ),
                    )
                }
            } catch (_: Exception) {
                throw RemoteException("IGDB is unavailable")
            }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> throw RemoteException("Resource not found")
            HttpStatusCode.BadRequest -> throw ServerException("Invalid request")
            HttpStatusCode.Unauthorized -> throw UnauthorizedException("Invalid API token")
            HttpStatusCode.ServiceUnavailable -> throw RemoteException("IGDB is unavailable")
            HttpStatusCode.TooManyRequests -> throw ServerException("Limit rate reached")
            else -> throw ServerException("Unexpected status code ${response.status}")
        }
    }
}
