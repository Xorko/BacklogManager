package com.xorko.backlogmanager.core.presentation.hooks

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.xorko.backlogmanager.core.presentation.SnackbarController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ObserveSnackbarEvents(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    ObserveAsEvents(
        flow = SnackbarController.events,
        key1 = snackbarHostState,
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result =
                snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.name,
                    duration = event.duration,
                    withDismissAction = event.isDismissable,
                )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }
}
