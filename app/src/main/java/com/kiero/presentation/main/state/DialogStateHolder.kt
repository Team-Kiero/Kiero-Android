package com.kiero.presentation.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kiero.core.model.trigger.DialogState

@Stable
class DialogStateHolder {
    var dialogState by mutableStateOf(DialogState())
        private set

    fun showDialog(onClick: () -> Unit) {
        dialogState = DialogState(isVisible = true, onClickAction = onClick)
    }

    fun dismissDialog() {
        dialogState = dialogState.copy(isVisible = false)
    }
}

@Composable
fun rememberDialogStateHolder(): DialogStateHolder = remember {
    DialogStateHolder()
}
