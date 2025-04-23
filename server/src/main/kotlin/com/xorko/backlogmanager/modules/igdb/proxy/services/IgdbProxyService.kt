package com.xorko.backlogmanager.modules.igdb.proxy.services

import com.xorko.backlogmanager.core.domain.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class IgdbProxyService(
    private val httpClient: HttpClient,
) {
    suspend fun forwardRequest(
        path: String,
        body: String,
    ): HttpResponse =
        httpClient.post(path) { setBody(body) }.also {
            Logger.i(
                message = "IGDB responded with status ${it.status}",
                tag = this::class.simpleName,
            )
        }
}
