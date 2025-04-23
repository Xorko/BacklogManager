package com.xorko.backlogmanager.modules.game.common.data

import com.xorko.backlogmanager.core.data.handleApiRequest
import com.xorko.backlogmanager.game.data.dto.igdb.IgdbGameDto
import com.xorko.backlogmanager.game.data.utils.IgdbQueryFactory
import com.xorko.backlogmanager.modules.game.games.data.api.IgdbPopularityGame
import com.xorko.backlogmanager.modules.game.games.data.api.IgdbRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class IgdbClient(
    private val httpClient: HttpClient,
) {
    suspend fun getGames(
        limit: Int,
        offset: Int,
    ): List<IgdbGameDto> =
        handleApiRequest {
            httpClient.post(IgdbRoutes.Games.ROUTE) {
                setBody(
                    IgdbQueryFactory.getGames(
                        limit = limit,
                        offset = offset,
                    ),
                )
            }
        }

    suspend fun getGameById(id: Long): List<IgdbGameDto> =
        handleApiRequest {
            httpClient.post(IgdbRoutes.Games.ROUTE) {
                setBody(
                    IgdbQueryFactory.getGameById(id = id),
                )
            }
        }

    suspend fun getGamesById(ids: List<Long>): List<IgdbGameDto> =
        handleApiRequest {
            httpClient.post(IgdbRoutes.Games.ROUTE) {
                setBody(
                    IgdbQueryFactory.getGamesById(
                        ids = ids,
                    ),
                )
            }
        }

    suspend fun getMostPlayedGamesOnSteam(
        limit: Int,
        offset: Int,
    ): List<IgdbPopularityGame> =
        handleApiRequest {
            httpClient.post(IgdbRoutes.Popularity.ROUTE) {
                setBody(
                    IgdbQueryFactory.getMostPlayedGamesOnSteam(
                        limit = limit,
                        offset = offset,
                    ),
                )
            }
        }
}
