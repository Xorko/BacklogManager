package com.xorko.backlogmanager.modules.igdb.proxy.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.domain.models.Game
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.time.Duration

class IgdbProxyService(
    private val httpClient: HttpClient,
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val requestCache =
        Caffeine
            .newBuilder()
            .expireAfterWrite(Duration.ofHours(6))
            .maximumSize(1_000)
            .buildAsync<Pair<String, String>, HttpResponse>()

    suspend fun forwardRequest(
        path: String,
        body: String,
    ): HttpResponse =
        requestCache
            .get(path to body) { _, _ ->
                scope.future {
                    Logger.d("Cache MISS for request to $path with body $body. Forwarding request to IGDB.")
                    httpClient.post(path) { setBody(body) }.also {
                        Logger.d(
                            message = "IGDB responded with status ${it.status}",
                            tag = this::class.simpleName,
                        )
                    }
                }
            }.await()
}
