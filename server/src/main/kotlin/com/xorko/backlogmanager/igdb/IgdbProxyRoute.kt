package com.xorko.backlogmanager.igdb

import com.xorko.backlogmanager.igdbHttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val LOGGER_NAME = "IgdbProxyRoute"
private val logger: Logger = LoggerFactory.getLogger(LOGGER_NAME)

fun Route.igdbProxyRoute() {
    route("/igdb{...}") {
        handle {
            val url = call.request.uri.removePrefix("/igdb")

            try {
                val response =
                    igdbHttpClient.post(url) {
                        setBody(call.receiveText())
                    }

                logger.info("IGDB responded with status ${response.status}")
                val body = response.bodyAsText()

                call.respondText(body, ContentType.Application.Json, response.status)
            } catch (e: Exception) {
                logger.error("Error while proxying request to IGDB", e)
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
