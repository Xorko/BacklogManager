package com.xorko.backlogmanager.core.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val baseHttpClientConfig: HttpClientConfig<CIOEngineConfig>.() -> Unit = {
    install(Logging) {
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            },
        )
    }
}

object BaseHttpClientFactory {
    fun create(config: (HttpClientConfig<CIOEngineConfig>.() -> Unit)? = null) =
        HttpClient(CIO) {
            baseHttpClientConfig()
            config?.invoke(this)
        }
}
