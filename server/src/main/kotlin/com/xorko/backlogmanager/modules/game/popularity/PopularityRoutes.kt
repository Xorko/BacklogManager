package com.xorko.backlogmanager.modules.game.popularity

import com.xorko.backlogmanager.DEFAULT_LIMIT
import com.xorko.backlogmanager.DEFAULT_OFFSET
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toSharedGameDto
import com.xorko.backlogmanager.modules.game.constants.GameRoutes
import com.xorko.backlogmanager.modules.game.constants.GameRoutes.Popular.Params.LIMIT
import com.xorko.backlogmanager.modules.game.constants.GameRoutes.Popular.Params.OFFSET
import com.xorko.backlogmanager.modules.game.games.services.GamesService
import com.xorko.backlogmanager.modules.game.popularity.services.PopularityService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.popularityRoutes() {
    val popularityService by inject<PopularityService>()
    val gamesService by inject<GamesService>()

    route(GameRoutes.Popular.ROUTE) {
        get {
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
