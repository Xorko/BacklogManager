package com.xorko.backlogmanager.modules.game.games

import com.xorko.backlogmanager.DEFAULT_LIMIT
import com.xorko.backlogmanager.DEFAULT_OFFSET
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toSharedGameDto
import com.xorko.backlogmanager.modules.game.constants.GameRoutes
import com.xorko.backlogmanager.modules.game.games.services.GamesService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.gamesRoute() {
    val gamesService by inject<GamesService>()

    route(GameRoutes.BASE_ROUTE) {
        get {
            val limit = call.request.queryParameters[GameRoutes.Params.LIMIT]?.toIntOrNull() ?: DEFAULT_LIMIT
            val offset = call.request.queryParameters[GameRoutes.Params.OFFSET]?.toIntOrNull() ?: DEFAULT_OFFSET

            val games = gamesService.getGames(limit = limit, offset = offset)
            call.respond(games.map { it.toSharedGameDto() })
        }
    }
}
