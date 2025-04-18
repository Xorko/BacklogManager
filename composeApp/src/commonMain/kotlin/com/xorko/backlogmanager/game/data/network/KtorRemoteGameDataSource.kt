package com.xorko.backlogmanager.game.data.network

import arrow.core.Either
import com.xorko.backlogmanager.core.data.configuration.apiConfig
import com.xorko.backlogmanager.core.data.safeCall
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.game.data.dto.GameDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

const val IGDB_ROUTE = "/igdb/v4"

class KtorRemoteGameDataSource(
    private val httpClient: HttpClient,
) : RemoteGameDataSource {
    override suspend fun searchGames(
        query: String,
        resultLimit: Int?,
    ): Either<DataError.Remote, List<GameDto>> =
        safeCall {
            httpClient.post(
                urlString = "${apiConfig.baseUrl}$IGDB_ROUTE/games",
            ) {
                contentType(ContentType.Text.Plain)
                setBody(
                    IgdbQueryFactory.searchGames(
                        query = query,
                        limit = resultLimit,
                    ),
                )
            }
        }
}
