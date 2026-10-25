package com.xorko.backlogmanager

import com.xorko.backlogmanager.core.domain.ApiError
import com.xorko.backlogmanager.core.domain.ApiException
import com.xorko.backlogmanager.core.domain.exceptions.ModuleException
import com.xorko.backlogmanager.di.appModules
import com.xorko.backlogmanager.modules.game.gameRoutes
import com.xorko.backlogmanager.modules.igdb.proxy.igdbProxyRoute
import io.ktor.http.CacheControl
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import kotlin.time.Duration.Companion.seconds

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

    install(RateLimit) {
        register {
            rateLimiter(limit = 100, refillPeriod = 60.seconds)
        }
    }

    install(StatusPages) {
        status(HttpStatusCode.TooManyRequests) { call, status ->
            call.respond(
                status = status,
                message =
                    ApiError(
                        error = status.description,
                        code = status.value,
                    ),
            )
        }

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

    install(CachingHeaders) {
        options { call, _ ->
            if (call.response.status()?.isSuccess() ?: true) {
                CachingOptions(
                    CacheControl.MaxAge(
                        maxAgeSeconds = 3600,
                        visibility = CacheControl.Visibility.Public,
                    ),
                )
            } else {
                null
            }
        }
    }

    routing {
        rateLimit {
            gameRoutes()
            igdbProxyRoute()
        }
    }
}
