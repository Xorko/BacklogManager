package com.xorko.backlogmanager

import com.xorko.backlogmanager.core.domain.exceptions.RemoteException
import com.xorko.backlogmanager.core.domain.exceptions.ServerException
import com.xorko.backlogmanager.core.domain.exceptions.UnauthorizedException
import com.xorko.backlogmanager.di.appModules
import com.xorko.backlogmanager.igdb.games.routes.gameRoutes
import com.xorko.backlogmanager.igdb.proxy.routes.igdbProxyRoute
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(appModules)
    }

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            },
        )
    }

    install(StatusPages) {
        exception<ServerException> { call, cause ->
            call.respondText(
                text = "Internal server error",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.InternalServerError,
            )
        }

        exception<RemoteException> { call, cause ->
            call.respondText(
                text = "Service unavailable",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.ServiceUnavailable,
            )
        }

        exception<UnauthorizedException> { call, cause ->
            call.respondText(
                text = "Unauthorized",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.Unauthorized,
            )
        }

        exception<Throwable> { call, cause ->
            call.respondText(
                text = "An unexpected error occurred",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.InternalServerError,
            )
        }
    }

    routing {
        get("/") {
            call.respondText("Server is up and running!")
        }
        gameRoutes()
        igdbProxyRoute()
    }
}
