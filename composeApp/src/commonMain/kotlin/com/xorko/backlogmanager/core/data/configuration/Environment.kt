package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig
import com.xorko.backlogmanager.core.domain.logging.Logger

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
    }.also {
        Logger.i("Current environment: $it", tag = Environment::class.simpleName)
        Logger.i("Current build type: ${currentBuildType.name}", tag = BuildType::class.simpleName)
    }
