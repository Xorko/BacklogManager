package com.xorko.backlogmanager.modules.game.games.data.api

import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.di.Qualifiers.BASE_HTTP_CLIENT
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

object IgdbTokenManager : KoinComponent {
    private val httpClient: HttpClient by inject(named(BASE_HTTP_CLIENT))

    private var token: String? = null
    private var tokenExpiresAt: Long = 0

    suspend fun getValidToken(): String {
        val currentTime = System.currentTimeMillis()
        if (token == null || currentTime >= tokenExpiresAt) {
            Logger.i(
                message = "Token is expired or not initialized. Refreshing token...",
                tag = this::class.simpleName,
            )
            refreshToken()
        }
        return token!!
    }

    private suspend fun refreshToken() {
        val response =
            httpClient
                .post(IgdbRoutes.TokenRetrieval.URL) {
                    url {
                        with(parameters) {
                            append(IgdbRoutes.TokenRetrieval.QueryParams.CLIENT_ID, IgdbConfig.clientId)
                            append(
                                IgdbRoutes.TokenRetrieval.QueryParams.CLIENT_SECRET,
                                IgdbConfig.clientSecret,
                            )
                            append(
                                IgdbRoutes.TokenRetrieval.QueryParams.GRANT_TYPE,
                                IgdbRoutes.TokenRetrieval.QueryParams.Values.CLIENT_CREDENTIALS,
                            )
                        }
                    }
                }.body<TokenResponse>()

        token = response.accessToken
        tokenExpiresAt = System.currentTimeMillis() + response.expiresIn * 1000
    }
}

@Serializable
private data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("token_type")
    val tokenType: String,
)
