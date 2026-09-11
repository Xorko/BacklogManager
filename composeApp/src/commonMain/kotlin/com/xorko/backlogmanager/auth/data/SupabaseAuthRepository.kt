package com.xorko.backlogmanager.auth.data

import com.xorko.backlogmanager.auth.domain.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class SupabaseAuthRepository(
    val supabaseAuth: Auth,
) : AuthRepository {
    private companion object {
        const val USERNAME_EXTRA_KEY = "username"
    }

    override suspend fun registerWithEmail(
        username: String,
        email: String,
        password: String,
    ) {
        supabaseAuth.signUpWith(Email) {
            this.email = email
            this.password = password
            this.data =
                buildJsonObject {
                    put(USERNAME_EXTRA_KEY, JsonPrimitive(username))
                }
        }
    }

    override suspend fun loginWithEmail(
        email: String,
        password: String,
    ) {
        supabaseAuth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }
}
