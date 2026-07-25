package com.xorko.backlogmanager

import com.xorko.backlogmanager.core.domain.ApiError
import com.xorko.backlogmanager.core.domain.ApiException
import com.xorko.backlogmanager.core.domain.exceptions.ModuleException
import com.xorko.backlogmanager.di.appModules
import com.xorko.backlogmanager.modules.game.gameRoutes
import com.xorko.backlogmanager.modules.igdb.proxy.igdbProxyRoute
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::main)
        .start(wait = true)
}

fun Application.main() {
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
        exception<ModuleException> { call, cause ->
            call.respond(
                status = cause.statusCode,
                message =
                    ApiError(
                        error = cause.message,
                        code = cause.statusCode.value,
                    ),
            )
        }

        exception<ApiException> { call, cause ->
            call.respond(
                status = cause.statusCode,
                message =
                    ApiError(
                        error = cause.message,
                        code = cause.statusCode.value,
                    ),
            )
        }

        exception<BadRequestException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message =
                    ApiError(
                        error = HttpStatusCode.BadRequest.description,
                        code = HttpStatusCode.BadRequest.value,
                    ),
            )
        }

        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message =
                    ApiError(
                        error = HttpStatusCode.InternalServerError.description,
                        code = HttpStatusCode.InternalServerError.value,
                    ),
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
