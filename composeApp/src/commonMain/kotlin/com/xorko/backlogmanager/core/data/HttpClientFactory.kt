package com.xorko.backlogmanager.core.data

import com.xorko.backlogmanager.core.data.configuration.currentBuildType
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.xorko.backlogmanager.core.domain.logging.Logger as AppLogger

object HttpClientFactory {
    fun create(engine: HttpClientEngine) =
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json =
                        Json {
                            ignoreUnknownKeys = true
                            explicitNulls = false
                        },
                )
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }

            defaultRequest {
                contentType(ContentType.Text.Plain)
            }

            if (currentBuildType.isDebug()) {
                AppLogger.i("Debug mode enabled. Enabling Ktor logging…", tag = this::class.simpleName)
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }
        }
}
