package com.kiero.presentation.splash.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.data.config.model.UpdateState

@Composable
fun AppUpdateDialog(
    updateState: UpdateState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isForce = updateState == UpdateState.FORCE

    KieroDialog(
        onDismiss = if (isForce) ({}) else onDismiss,
        modifier = modifier,
        title = "새로운 업데이트가 있어요",
        subDescription = if (isForce) {
            "원활한 서비스 이용을 위해\n최신 버전으로 업데이트해주세요"
        } else {
            "새로운 기능과 개선사항을 만나보세요"
        },
        isDisabled = isForce,
        cancelAction = if (isForce) null else KieroCancelAction(text = "다음에", onClick = onDismiss),
        confirmAction = KieroConfirmAction(text = "업데이트", onClick = onConfirm),
    )
}

@Preview
@Composable
private fun AppUpdateDialogForcePreview() {
    KieroTheme {
        AppUpdateDialog(
            updateState = UpdateState.FORCE,
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun AppUpdateDialogFlexiblePreview() {
    KieroTheme {
        AppUpdateDialog(
            updateState = UpdateState.FLEXIBLE,
            onConfirm = {},
            onDismiss = {},
        )
    }
}
