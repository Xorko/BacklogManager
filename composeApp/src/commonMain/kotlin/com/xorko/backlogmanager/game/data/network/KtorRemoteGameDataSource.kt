package com.xorko.backlogmanager.game.data.network

import arrow.core.Either
import com.xorko.backlogmanager.core.data.configuration.apiConfig
import com.xorko.backlogmanager.core.data.safeCall
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.data.dto.igdb.IgdbGameDto
import com.xorko.backlogmanager.game.data.dto.shared.SharedGameDto
import com.xorko.backlogmanager.game.data.utils.IgdbQueryFactory
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters

private const val IGDB_ROUTE = "/igdb/proxy"
private const val LIMIT_PARAM = "limit"
private const val OFFSET_PARAM = "offset"

class KtorRemoteGameDataSource(
    private val httpClient: HttpClient,
) : RemoteGameDataSource {
    override suspend fun searchGames(
        query: String,
        resultLimit: Int?,
    ): Either<DataError.Remote, List<IgdbGameDto>> =
        safeCall {
            httpClient.post(
                urlString = "${apiConfig.baseUrl}$IGDB_ROUTE/games",
            ) {
                setBody(
                    IgdbQueryFactory.searchGames(
                        query = query,
                        limit = resultLimit,
                    ),
                )
            }
        }

    override suspend fun getGames(
        resultLimit: Int?,
        resultOffset: Int?,
    ): Either<DataError.Remote, List<SharedGameDto>> =
        safeCall {
            httpClient.get(
                urlString = "${apiConfig.baseUrl}/game",
            ) {
                parameters {
                    resultLimit?.let { parameter(LIMIT_PARAM, it) }
                    resultOffset?.let { parameter(OFFSET_PARAM, it) }
                }
            }
        }

    override suspend fun getPopularGames(
        resultLimit: Int?,
        resultOffset: Int?,
    ): Either<DataError.Remote, List<SharedGameDto>> =
        safeCall {
            httpClient.get(
                urlString = "${apiConfig.baseUrl}/game/popular",
            ) {
                parameters {
                    resultLimit?.let { parameter(LIMIT_PARAM, it) }
                    resultOffset?.let { parameter(OFFSET_PARAM, it) }
                }
            }
        }

    override suspend fun getGameById(id: Long): Either<DataError.Remote, SharedGameDto?> =
        safeCall {
            httpClient.get(
                urlString = "${apiConfig.baseUrl}/game/$id",
            )
        }
}
