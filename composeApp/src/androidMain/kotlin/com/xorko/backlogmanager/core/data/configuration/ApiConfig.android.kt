package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig
import com.xorko.backlogmanager.SERVER_PORT

actual val apiConfig: ApiConfig = AndroidApiConfig

object AndroidApiConfig : ApiConfig {
    override val baseUrl: String = "${BuildKonfig.API_BASE_URL}:$SERVER_PORT"
}
