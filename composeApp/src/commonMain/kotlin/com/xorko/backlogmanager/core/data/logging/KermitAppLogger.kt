package com.xorko.backlogmanager.core.data.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import com.xorko.backlogmanager.core.domain.logging.AppLogger

object KermitAppLogger : AppLogger {
    private const val APP_TAG = "BacklogManager"

    val logger =
        Logger(
            config =
                StaticConfig(
                    minSeverity = Severity.Verbose,
                    logWriterList = listOf(platformLogWriter()),
                ),
        ).withTag(APP_TAG)

    override fun e(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.e(tag = tag ?: APP_TAG, throwable = throwable) { message }

    override fun w(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.w(tag = tag ?: APP_TAG, throwable = throwable) { message }

    override fun i(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.i(tag = tag ?: APP_TAG, throwable = throwable) { message }

    override fun d(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.d(tag = tag ?: APP_TAG, throwable = throwable) { message }

    override fun v(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.v(tag = tag ?: APP_TAG, throwable = throwable) { message }
}
