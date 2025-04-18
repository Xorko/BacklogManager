package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig
import com.xorko.backlogmanager.SERVER_PORT
import com.xorko.backlogmanager.core.domain.logging.Logger

actual val apiConfig: ApiConfig = AndroidApiConfig

object AndroidApiConfig : ApiConfig {
    override val baseUrl: String =
        "${BuildKonfig.API_BASE_URL}:$SERVER_PORT".also {
            Logger.i("API base URL: $it", tag = this::class.simpleName)
        }
}
