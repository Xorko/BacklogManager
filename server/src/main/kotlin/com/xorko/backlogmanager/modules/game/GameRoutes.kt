package com.xorko.backlogmanager.modules.game

import com.xorko.backlogmanager.modules.game.games.gamesRoute
import com.xorko.backlogmanager.modules.game.popularity.popularityRoutes
import io.ktor.server.routing.Route

fun Route.gameRoutes() {
    gamesRoute()
    popularityRoutes()
}
