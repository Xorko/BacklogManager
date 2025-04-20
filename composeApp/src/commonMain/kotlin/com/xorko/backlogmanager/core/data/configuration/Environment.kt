package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig
import com.xorko.backlogmanager.utils.enumValueOfOrNull

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
    enumValueOfOrNull<Environment>(BuildKonfig.env.uppercase())
        ?: error(
            """
            Invalid environment value: ${BuildKonfig.env}.
            Please set one of the following values: ${Environment.entries.joinToString(", ")}
            """.trimIndent(),
        )
