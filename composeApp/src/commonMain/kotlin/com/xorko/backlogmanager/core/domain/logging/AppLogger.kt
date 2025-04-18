package com.xorko.backlogmanager.core.domain.logging

interface AppLogger {
    fun e(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    )

    fun w(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    )

    fun i(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    )

    fun d(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    )

    fun v(
        message: String,
        tag: String? = null,
        throwable: Throwable? = null,
    )
}
