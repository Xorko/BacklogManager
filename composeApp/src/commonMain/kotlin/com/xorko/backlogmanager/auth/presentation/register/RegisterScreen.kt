package com.xorko.backlogmanager.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.register_screen_already_have_account
import backlogmanager.composeapp.generated.resources.register_screen_email_label
import backlogmanager.composeapp.generated.resources.register_screen_password_label
import backlogmanager.composeapp.generated.resources.register_screen_register_button_label
import backlogmanager.composeapp.generated.resources.register_screen_title
import backlogmanager.composeapp.generated.resources.register_screen_username_label
import com.xorko.backlogmanager.core.presentation.hooks.ObserveSnackbarEvents
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel,
    onAlreadyHaveAnAccountClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is RegisterActions.OnAlreadyHaveAnAccountClicked -> onAlreadyHaveAnAccountClicked()
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterActions) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveSnackbarEvents(
        snackbarHostState = snackbarHostState,
        scope = scope,
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            RegisterForm(
                state = state,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun RegisterForm(
    state: RegisterState,
    onAction: (RegisterActions) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = 400.dp)
                    .padding(24.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.register_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            OutlinedTextField(
                value = state.username,
                onValueChange = { onAction(RegisterActions.OnUsernameInputChanged(username = it)) },
                label = { Text(stringResource(Res.string.register_screen_username_label)) },
                singleLine = true,
                isError = state.usernameError != null,
                supportingText = {
                    if (state.usernameError != null && state.usernameTouched) {
                        Text(
                            text = state.usernameError.asString(),
                            modifier = Modifier.clearAndSetSemantics {},
                        )
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (!it.isFocused) {
                                onAction(RegisterActions.OnUsernameInputFocusLost)
                            }
                        },
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = { onAction(RegisterActions.OnEmailInputChanged(email = it)) },
                label = { Text(stringResource(Res.string.register_screen_email_label)) },
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onNext = {
                            onAction(RegisterActions.OnEmailInputFocusLost)
                        },
                    ),
                isError = state.emailError != null,
                supportingText = {
                    if (state.emailError != null && state.emailTouched) {
                        Text(
                            text = state.emailError.asString(),
                            modifier = Modifier.clearAndSetSemantics {},
                        )
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (!it.isFocused) {
                                onAction(RegisterActions.OnEmailInputFocusLost)
                            }
                        },
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = { onAction(RegisterActions.OnPasswordInputChanged(password = it)) },
                label = { Text(stringResource(Res.string.register_screen_password_label)) },
                singleLine = true,
                isError = state.passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = {
                    if (state.passwordError != null && state.passwordTouched) {
                        Text(
                            text = state.passwordError.asString(),
                            modifier = Modifier.clearAndSetSemantics {},
                        )
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (!it.isFocused) {
                                onAction(RegisterActions.OnPasswordInputFocusLost)
                            }
                        },
            )
            Button(
                onClick = {
                    onAction(
                        RegisterActions.OnRegisterClicked,
                    )
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(stringResource(Res.string.register_screen_register_button_label))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { onAction(RegisterActions.OnAlreadyHaveAnAccountClicked) }) {
                Text(stringResource(Res.string.register_screen_already_have_account))
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    BacklogManagerTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
        )
    }
}
