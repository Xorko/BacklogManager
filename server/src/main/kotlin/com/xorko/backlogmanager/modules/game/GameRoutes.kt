package com.xorko.backlogmanager.modules.game

import com.xorko.backlogmanager.DEFAULT_LIMIT
import com.xorko.backlogmanager.DEFAULT_OFFSET
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toSharedGameDto
import com.xorko.backlogmanager.modules.game.constants.GameRoutes
import com.xorko.backlogmanager.modules.game.constants.GameRoutes.Popular.QueryParams.LIMIT
import com.xorko.backlogmanager.modules.game.constants.GameRoutes.Popular.QueryParams.OFFSET
import com.xorko.backlogmanager.modules.game.games.services.GamesService
import com.xorko.backlogmanager.modules.game.popularity.services.PopularityService
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.gameRoutes() {
    val gamesService by inject<GamesService>()
    val popularityService by inject<PopularityService>()

    route(GameRoutes.BASE_ROUTE) {
        get {
            val limit =
                call.request.queryParameters[GameRoutes.QueryParams.LIMIT]?.toIntOrNull()
                    ?: DEFAULT_LIMIT
            val offset =
                call.request.queryParameters[GameRoutes.QueryParams.OFFSET]?.toIntOrNull()
                    ?: DEFAULT_OFFSET

            val games = gamesService.getGames(limit = limit, offset = offset)
            call.respond(games.map { it.toSharedGameDto() })
        }

        get(GameRoutes.ById.ROUTE) {
            val id =
                call.parameters[GameRoutes.ById.Params.GAME_ID]?.toLongOrNull()
                    ?: throw BadRequestException("Invalid game id")

            val game = gamesService.getGameById(id)
            call.respond(
                game.toSharedGameDto(),
            )
        }

        get(GameRoutes.Popular.ROUTE) {
            val limit = call.request.queryParameters[LIMIT]?.toIntOrNull() ?: DEFAULT_LIMIT
            val offset = call.request.queryParameters[OFFSET]?.toIntOrNull() ?: DEFAULT_OFFSET

            val mostPlayedGamesOnSteamIds =
                popularityService
                    .getMostPlayedGamesOnSteam(
                        limit = limit,
                        offset = offset,
                    )

            val mostPlayedGamesOnSteam = gamesService.getGamesByIds(mostPlayedGamesOnSteamIds)
            call.respond(mostPlayedGamesOnSteam.map { it.toSharedGameDto() })
        }
    }
}
