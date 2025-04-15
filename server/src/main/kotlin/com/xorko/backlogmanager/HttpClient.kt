package com.xorko.backlogmanager

import com.xorko.backlogmanager.igdb.IgdbConfig
import com.xorko.backlogmanager.igdb.IgdbTokenManager
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val baseHttpClientConfig: HttpClientConfig<CIOEngineConfig>.() -> Unit = {
    install(Logging)
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            },
        )
    }
}

val baseHttpClient = HttpClient(CIO, baseHttpClientConfig)

val igdbHttpClient =
    HttpClient(CIO) {
        baseHttpClientConfig()
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = IgdbTokenManager.getValidToken(),
                        refreshToken = null,
                    )
                }
                refreshTokens {
                    BearerTokens(
                        accessToken = IgdbTokenManager.getValidToken(),
                        refreshToken = null,
                    )
                }
            }
        }
        defaultRequest {
            url(IgdbConfig.BASE_URL)
            header(IgdbConfig.CLIENT_ID_HEADER, IgdbConfig.clientId)
        }
    }
