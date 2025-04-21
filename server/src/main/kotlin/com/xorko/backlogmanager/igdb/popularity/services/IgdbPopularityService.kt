package com.xorko.backlogmanager.igdb.popularity.services

import com.xorko.backlogmanager.core.domain.exceptions.RemoteException
import com.xorko.backlogmanager.core.domain.exceptions.ServerException
import com.xorko.backlogmanager.core.domain.exceptions.UnauthorizedException
import com.xorko.backlogmanager.game.data.utils.IgdbQueryFactory
import com.xorko.backlogmanager.igdb.popularity.models.IgdbPopularityGame
import com.xorko.backlogmanager.igdbHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

private const val IGDB_POPULARITY_ROUTE = "popularity_primitives"

class IgdbPopularityService {
    suspend fun getCurrentlyMostPlayedGamesOnSteam(
        limit: Int?,
        offset: Int?,
    ): List<IgdbPopularityGame> {
        val response =
            try {
                igdbHttpClient.post(IGDB_POPULARITY_ROUTE) {
                    setBody(
                        IgdbQueryFactory.getMostPlayedGamesOnSteam(
                            limit = limit,
                            offset = offset,
                        ),
                    )
                }
            } catch (_: Exception) {
                throw RemoteException("IGDB popularity is unavailable")
            }

        return when (response.status) {
            HttpStatusCode.Companion.OK -> response.body()
            HttpStatusCode.Companion.NotFound -> throw RemoteException("Resource not found")
            HttpStatusCode.Companion.BadRequest -> throw ServerException("Invalid request")
            HttpStatusCode.Companion.Unauthorized -> throw UnauthorizedException("Invalid API token")
            HttpStatusCode.Companion.ServiceUnavailable -> throw RemoteException("IGDB is unavailable")
            HttpStatusCode.Companion.TooManyRequests -> throw ServerException("Limit rate reached")
            else -> throw ServerException("Unexpected status code ${response.status}")
        }
    }
}
