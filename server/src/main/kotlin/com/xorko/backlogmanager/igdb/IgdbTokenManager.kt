package com.xorko.backlogmanager.igdb

import com.xorko.backlogmanager.baseHttpClient
import com.xorko.backlogmanager.igdb.IgdbTokenManager.QueryParameters.CLIENT_ID_KEY
import com.xorko.backlogmanager.igdb.IgdbTokenManager.QueryParameters.CLIENT_SECRET_KEY
import com.xorko.backlogmanager.igdb.IgdbTokenManager.QueryParameters.GRANT_TYPE_KEY
import com.xorko.backlogmanager.igdb.IgdbTokenManager.QueryParameters.GRANT_TYPE_VALUE
import com.xorko.backlogmanager.utils.logger
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object IgdbTokenManager {
    private val logger = logger<IgdbTokenManager>()

    private var token: String? = null
    private var tokenExpiresAt: Long = 0

    private object QueryParameters {
        const val CLIENT_ID_KEY = "client_id"
        const val CLIENT_SECRET_KEY = "client_secret"
        const val GRANT_TYPE_KEY = "grant_type"
        const val GRANT_TYPE_VALUE = "client_credentials"
    }

    suspend fun getValidToken(): String {
        val currentTime = System.currentTimeMillis()
        if (token == null || currentTime >= tokenExpiresAt) {
            logger.info("Token is expired or not initialized. Refreshing token...")
            refreshToken()
        }
        return token!!
    }

    private suspend fun refreshToken() {
        val response =
            baseHttpClient
                .submitForm(
                    url = IgdbConfig.TOKEN_URL,
                    formParameters =
                        Parameters.build {
                            append(CLIENT_ID_KEY, IgdbConfig.clientId)
                            append(CLIENT_SECRET_KEY, IgdbConfig.clientSecret)
                            append(GRANT_TYPE_KEY, GRANT_TYPE_VALUE)
                        },
                ).body<TokenResponse>()

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
