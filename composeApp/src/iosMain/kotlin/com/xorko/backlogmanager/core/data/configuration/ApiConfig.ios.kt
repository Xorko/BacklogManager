package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig

actual val apiConfig: ApiConfig = IosApiConfig

object IosApiConfig : ApiConfig {
    override val baseUrl: String = BuildKonfig.API_BASE_URL
}
