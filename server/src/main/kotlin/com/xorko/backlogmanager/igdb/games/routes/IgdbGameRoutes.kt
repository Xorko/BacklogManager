package com.xorko.backlogmanager.igdb.games.routes

import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.data.dto.igdb.mappers.toGame
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toSharedGameDto
import com.xorko.backlogmanager.igdb.games.services.IgdbGameService
import com.xorko.backlogmanager.igdb.popularity.usecases.GetDetailsOnPopularGamesUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

private const val GAMES_ROUTE = "/games"

private const val LIMIT_PARAM = "limit"
private const val OFFSET_PARAM = "offset"

private const val DEFAULT_LIMIT = 30
private const val DEFAULT_OFFSET = 0

fun Route.gameRoutes() {
    val gameService by inject<IgdbGameService>()
    val getDetailsOnPopularGamesUseCase by inject<GetDetailsOnPopularGamesUseCase>()

    route(GAMES_ROUTE) {
        get {
            val limit = call.request.queryParameters[LIMIT_PARAM]?.toIntOrNull() ?: DEFAULT_LIMIT
            val offset = call.request.queryParameters[OFFSET_PARAM]?.toIntOrNull() ?: DEFAULT_OFFSET
            Logger.d(
                message = "Fetching games from IGDB. Limit: $limit, Offset: $offset",
                tag = this::class.simpleName,
            )

            try {
                val games =
                    gameService
                        .getGames(
                            limit = limit,
                            offset = offset,
                        ).map { it.toGame().toSharedGameDto() }
                call.respond(games)
            } catch (e: Exception) {
                Logger.e(
                    message = "Error while fetching games from IGDB",
                    tag = this::class.simpleName,
                    throwable = e,
                )
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    route("$GAMES_ROUTE/popular") {
        get {
            val limit = call.request.queryParameters[LIMIT_PARAM]?.toIntOrNull() ?: DEFAULT_LIMIT
            val offset = call.request.queryParameters[OFFSET_PARAM]?.toIntOrNull() ?: DEFAULT_OFFSET
            Logger.d(
                message = "Fetching popular games from IGDB. Limit: $limit, Offset: $offset",
                tag = this::class.simpleName,
            )

            try {
                val popularGames =
                    getDetailsOnPopularGamesUseCase(
                        limit = limit,
                        offset = offset,
                    ).map {
                        it.toGame().toSharedGameDto()
                    }
                call.respond(popularGames)
            } catch (e: Exception) {
                Logger.e(
                    message = "Error while fetching popular games from IGDB",
                    tag = this::class.simpleName,
                    throwable = e,
                )
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
