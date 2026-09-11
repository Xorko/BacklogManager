package com.xorko.backlogmanager.auth.presentation.register

interface RegisterActions {
    data class OnUsernameInputChanged(
        val username: String,
    ) : RegisterActions

    data object OnUsernameInputFocusLost : RegisterActions

    data class OnEmailInputChanged(
        val email: String,
    ) : RegisterActions

    data object OnEmailInputFocusLost : RegisterActions

    data class OnPasswordInputChanged(
        val password: String,
    ) : RegisterActions

    data object OnPasswordInputFocusLost : RegisterActions

    data object OnRegisterClicked : RegisterActions

    data object OnLoginClicked : RegisterActions

    data object OnAlreadyHaveAnAccountClicked : RegisterActions
}
