package com.xorko.backlogmanager.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.register_screen_email_error_blank
import backlogmanager.composeapp.generated.resources.register_screen_email_error_invalid_format
import backlogmanager.composeapp.generated.resources.register_screen_password_error_blank
import backlogmanager.composeapp.generated.resources.register_screen_username_error_blank
import com.xorko.backlogmanager.auth.domain.AuthRepository
import com.xorko.backlogmanager.core.presentation.EMAIL_ADDRESS_REGEX
import com.xorko.backlogmanager.core.presentation.SnackbarController
import com.xorko.backlogmanager.core.presentation.SnackbarEvent
import com.xorko.backlogmanager.core.presentation.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private companion object {
    }

    private val _state = MutableStateFlow(RegisterState())
    val state =
        _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value,
        )

    fun onAction(action: RegisterActions) {
        when (action) {
            is RegisterActions.OnUsernameInputChanged -> {
                _state.update {
                    it.copy(
                        username = action.username,
                        usernameError = validateUsername(action.username),
                    )
                }
            }

            is RegisterActions.OnUsernameInputFocusLost -> {
                _state.update {
                    it.copy(
                        usernameTouched = true,
                        usernameError = validateUsername(state.value.username),
                    )
                }
            }

            is RegisterActions.OnEmailInputChanged -> {
                _state.update {
                    it.copy(
                        email = action.email,
                        emailError = validateEmail(action.email),
                    )
                }
            }

            is RegisterActions.OnEmailInputFocusLost -> {
                _state.update {
                    it.copy(
                        emailTouched = true,
                        emailError = validateEmail(state.value.email),
                    )
                }
            }

            is RegisterActions.OnPasswordInputChanged -> {
                _state.update {
                    it.copy(
                        password = action.password,
                        passwordError = isPasswordValid(action.password),
                    )
                }
            }

            is RegisterActions.OnPasswordInputFocusLost -> {
                _state.update {
                    it.copy(
                        passwordTouched = true,
                        passwordError = isPasswordValid(state.value.password),
                    )
                }
            }

            is RegisterActions.OnRegisterClicked -> {
                registerUser()
            }
            is RegisterActions.OnLoginClicked -> notImplementedYet()
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.registerWithEmail(
                username = state.value.username,
                email = state.value.email,
                password = state.value.password,
            )
            _state.update { it.copy(isLoading = false) }
            SnackbarController.sendEvent(
                SnackbarEvent(
                    message = "User registered",
                ),
            )
        }
    }

    private fun validateEmail(email: String): UiText? {
        if (!state.value.emailTouched) {
            return null
        }
        if (email.isBlank()) {
            return UiText.StringResourceId(Res.string.register_screen_email_error_blank)
        }
        if (!EMAIL_ADDRESS_REGEX.matches(email)) {
            return UiText.StringResourceId(Res.string.register_screen_email_error_invalid_format)
        }
        return null
    }

    private fun isPasswordValid(password: String): UiText? {
        if (!state.value.passwordTouched) {
            return null
        }
        if (password.isBlank()) {
            return UiText.StringResourceId(Res.string.register_screen_password_error_blank)
        }
        return null
    }

    private fun validateUsername(username: String): UiText? {
        if (!state.value.usernameTouched) {
            return null
        }
        if (username.isBlank()) {
            return UiText.StringResourceId(Res.string.register_screen_username_error_blank)
        }
        // TODO: Add availability check
        return null
    }

    fun notImplementedYet() =
        viewModelScope.launch {
            SnackbarController.sendEvent(
                SnackbarEvent(
                    message = "Not implemented yet",
                ),
            )
        }
}
