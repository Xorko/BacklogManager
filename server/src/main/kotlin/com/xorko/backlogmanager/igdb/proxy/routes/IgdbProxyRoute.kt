package com.xorko.backlogmanager.igdb.proxy.routes

import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.igdb.proxy.services.IgdbProxyService
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

private const val IGDB_PROXY_BASE_ROUTE = "/igdb/proxy"

fun Route.igdbProxyRoute(proxyService: IgdbProxyService) {
    route("$IGDB_PROXY_BASE_ROUTE{...}") {
        handle {
            val url = call.request.uri.removePrefix(IGDB_PROXY_BASE_ROUTE)
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
