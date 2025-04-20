package com.xorko.backlogmanager.core.data.logging

import com.xorko.backlogmanager.core.domain.logging.AppLogger
import com.xorko.backlogmanager.core.domain.logging.Logger
import org.slf4j.LoggerFactory

/**
 * [AppLogger] implementation using SLF4J [org.slf4j.Logger].
 *
 * Due to how [Logger] is implemented and because it is shared between the client and the server,
 * the class passed to [LoggerFactory.getLogger] will always be [Slf4jLogger]. If you want to
 * display the name of the calling class, please use the tag argument in log methods.
 *
 * @see AppLogger
 * @see Logger
 */
class Slf4jLogger : AppLogger {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun buildMessage(
        message: String,
        tag: String?,
    ) = if (tag.isNullOrBlank()) message else "[$tag] $message"

    override fun e(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.error(buildMessage(message, tag), throwable)

    override fun w(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.warn(buildMessage(message, tag), throwable)

    override fun i(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.info(buildMessage(message, tag), throwable)

    override fun d(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.debug(buildMessage(message, tag), throwable)

    override fun v(
        message: String,
        tag: String?,
        throwable: Throwable?,
    ) = logger.trace(buildMessage(message, tag), throwable)
}
