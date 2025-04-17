package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig

actual val apiConfig: ApiConfig = AndroidApiConfig

object AndroidApiConfig : ApiConfig {
    override val baseUrl: String = BuildKonfig.API_BASE_URL
}
