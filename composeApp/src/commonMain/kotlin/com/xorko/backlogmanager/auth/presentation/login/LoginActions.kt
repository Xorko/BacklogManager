package com.xorko.backlogmanager.auth.presentation.login

interface LoginActions {
    data class OnLoginClicked(
        val email: String,
        val password: String,
    ) : LoginActions

    data class OnEmailInputChanged(
        val email: String,
    ) : LoginActions

    data class OnPasswordInputChanged(
        val password: String,
    ) : LoginActions

    object OnRegisterClicked : LoginActions
}
