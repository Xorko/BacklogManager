package com.xorko.backlogmanager.auth.domain

interface AuthRepository {
    suspend fun registerWithEmail(
        username: String,
        email: String,
        password: String,
    )

    suspend fun loginWithEmail(
        email: String,
        password: String,
    )
}
