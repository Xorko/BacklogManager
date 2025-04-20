package com.xorko.backlogmanager.core.domain.logging

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A logger that uses the injected [AppLogger] to log messages.
 *
 * It is used to call log functions statically and avoids using the implementation directly to do so.
 */
object Logger : AppLogger, KoinComponent {
    private val appLogger: AppLogger by inject()

    override fun e(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = appLogger.e(message, tag, throwable)

    override fun w(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = appLogger.w(message, tag, throwable)

    override fun i(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = appLogger.i(message, tag, throwable)

    override fun d(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = appLogger.d(message, tag, throwable)

    override fun v(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = appLogger.v(message, tag, throwable)
}
