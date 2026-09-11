package com.xorko.backlogmanager.auth.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
)
