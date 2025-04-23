package com.xorko.backlogmanager.core.domain

import io.ktor.http.HttpStatusCode

class ApiException(
    override val message: String,
    val statusCode: HttpStatusCode,
) : Exception()
