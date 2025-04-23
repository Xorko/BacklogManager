package com.xorko.backlogmanager.modules.igdb.proxy

import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.modules.igdb.proxy.constants.IgdbProxyRoutes
import com.xorko.backlogmanager.modules.igdb.proxy.services.IgdbProxyService
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.igdbProxyRoute() {
    val proxyService by inject<IgdbProxyService>()
    route("${IgdbProxyRoutes.BASE_ROUTE}{...}") {
        handle {
            val url = call.request.uri.removePrefix(IgdbProxyRoutes.BASE_ROUTE)
            val body = call.receiveText()

            try {
                val response =
                    proxyService.forwardRequest(
                        path = url,
                        body = body,
                    )

                call.respondText(
                    text = response.bodyAsText(),
                    contentType = ContentType.Application.Json,
                    status = response.status,
                )
            } catch (e: Exception) {
                Logger.e(
                    message = "Error while proxying request to IGDB",
                    tag = this::class.simpleName,
                    throwable = e,
                )
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
