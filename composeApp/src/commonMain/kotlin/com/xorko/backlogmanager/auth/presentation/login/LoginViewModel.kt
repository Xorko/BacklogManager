package com.xorko.backlogmanager.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xorko.backlogmanager.auth.domain.AuthRepository
import com.xorko.backlogmanager.core.presentation.SnackbarController
import com.xorko.backlogmanager.core.presentation.SnackbarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state =
        _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value,
        )

    fun onAction(action: LoginActions) {
        when (action) {
            is LoginActions.OnLoginClicked -> notImplementedYet()

            is LoginActions.OnEmailInputChanged -> {
                _state.update {
                    it.copy(
                        email = action.email,
                    )
                }
            }

            is LoginActions.OnPasswordInputChanged -> {
                _state.update {
                    it.copy(
                        password = action.password,
                    )
                }
            }
        }
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
