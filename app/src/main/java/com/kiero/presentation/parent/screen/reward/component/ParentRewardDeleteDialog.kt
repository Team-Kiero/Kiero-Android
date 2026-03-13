package com.kiero.presentation.parent.screen.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.reward.model.ParentRewardUiModel

@Composable
fun ParentRewardDeleteDialog(
    reward: ParentRewardUiModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KieroDialog(
        onDismiss = onDismiss,
        modifier = modifier,
        title = reward.name,
        subDescription = "삭제하시겠습니까?",
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.img_coin),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "${reward.price} 개",
                        style = KieroTheme.typography.semiBold.title3,
                        color = KieroTheme.colors.main
                    )
                }
            }
        },
        cancelAction = KieroCancelAction(
            text = "취소",
            onClick = onDismiss,
        ),
        confirmAction = KieroConfirmAction(
            text = "확인",
            onClick = onConfirm,
        ),
    )
}

@Preview
@Composable
private fun ParentRewardDeleteDialogPreview() {
    KieroTheme {
        ParentRewardDeleteDialog(
            reward = ParentRewardUiModel(
                couponId = 1L,
                name = "용돈 5000원 받기",
                price = 350,
            ),
            onDismiss = {},
            onConfirm = {},
        )
    }
}