package com.xorko.backlogmanager.auth.presentation.register

import com.xorko.backlogmanager.core.presentation.UiText

data class RegisterState(
    val username: String = "",
    val usernameTouched: Boolean = false,
    val usernameError: UiText? = null,
    val email: String = "",
    val emailTouched: Boolean = false,
    val emailError: UiText? = null,
    val password: String = "",
    val passwordTouched: Boolean = false,
    val passwordError: UiText? = null,
    val isLoading: Boolean = false,
)
