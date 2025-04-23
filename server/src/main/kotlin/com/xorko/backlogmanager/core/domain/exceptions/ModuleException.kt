package com.xorko.backlogmanager.core.domain.exceptions

import io.ktor.http.HttpStatusCode

open class ModuleException(
    override val message: String,
    val statusCode: HttpStatusCode,
) : Exception(message)
