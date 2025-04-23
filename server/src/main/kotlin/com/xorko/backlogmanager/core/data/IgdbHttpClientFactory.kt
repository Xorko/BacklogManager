package com.xorko.backlogmanager.core.data

import com.xorko.backlogmanager.modules.game.games.data.api.IgdbConfig
import com.xorko.backlogmanager.modules.game.games.data.api.IgdbRoutes
import com.xorko.backlogmanager.modules.game.games.data.api.IgdbTokenManager
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header

object IgdbHttpClientFactory {
    fun create() =
        BaseHttpClientFactory.create {
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
                url(IgdbRoutes.BASE_URL)
                header(IgdbRoutes.Headers.CLIENT_ID, IgdbConfig.clientId)
            }
        }
}
