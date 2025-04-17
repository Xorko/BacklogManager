package com.xorko.backlogmanager.core.data.configuration

expect val apiConfig: ApiConfig

interface ApiConfig {
    val baseUrl: String
}
