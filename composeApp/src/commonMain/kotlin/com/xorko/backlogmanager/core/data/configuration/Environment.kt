package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig

enum class Environment {
    DEV,
    STAGING,
    PRODUCTION,
    ;

    fun isLocal() = this == DEV

    fun isStaging() = this == STAGING

    fun isProduction() = this == PRODUCTION
}

val currentEnvironment: Environment =
    when (BuildKonfig.env) {
        "dev" -> Environment.DEV
        "staging" -> Environment.STAGING
        "prod" -> Environment.PRODUCTION
        else -> error("Unknown environment: ${BuildKonfig.env}")
    }
