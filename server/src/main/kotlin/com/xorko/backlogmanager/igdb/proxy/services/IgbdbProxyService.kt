package com.xorko.backlogmanager.igdb.proxy.services

import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.igdbHttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class IgdbProxyService {
    suspend fun forwardRequest(
        path: String,
        body: String,
    ): HttpResponse =
        igdbHttpClient.post(path) { setBody(body) }.also {
            Logger.i(
                message = "IGDB responded with status ${it.status}",
                tag = this::class.simpleName,
            )
        }
}
