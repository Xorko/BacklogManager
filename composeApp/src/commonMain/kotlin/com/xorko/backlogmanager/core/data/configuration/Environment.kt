package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig

enum class Environment {
    LOCAL,
    STAGING,
    PRODUCTION,
}

val currentEnvironment: Environment =
    when (BuildKonfig.env) {
        "dev" -> Environment.LOCAL
        "staging" -> Environment.STAGING
        "prod" -> Environment.PRODUCTION
        else -> error("Unknown environment: ${BuildKonfig.env}")
    }
