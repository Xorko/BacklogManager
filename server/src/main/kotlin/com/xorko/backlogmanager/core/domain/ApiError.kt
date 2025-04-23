package com.xorko.backlogmanager.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: Int,
    val error: String,
)
