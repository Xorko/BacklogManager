package com.xorko.backlogmanager.core.data.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import com.xorko.backlogmanager.core.data.configuration.currentEnvironment

object AppLogger {
    private const val APP_TAG = "BacklogManager"

    val logger =
        Logger(
            config =
                StaticConfig(
                    minSeverity = currentEnvironment.toMinSeverity(),
                ),
        ).withTag(APP_TAG)

    fun e(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    ) = logger.e(tag = tag ?: APP_TAG, throwable = throwable) { message }

    fun w(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    ) = logger.w(tag = tag ?: APP_TAG, throwable = throwable) { message }

    fun i(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    ) = logger.i(tag = tag ?: APP_TAG, throwable = throwable) { message }

    fun d(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    ) = logger.d(tag = tag ?: APP_TAG, throwable = throwable) { message }

    fun v(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    ) = logger.v(tag = tag ?: APP_TAG, throwable = throwable) { message }
}
