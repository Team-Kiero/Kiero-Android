package com.kiero.core.designsystem.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kiero.R
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.dialog.action.DialogAction
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subDescription: String? = null,
    confirmAction: DialogAction,
    cancelAction: DialogAction? = null,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false
    ),
    isDisabled: Boolean = false, // x 버튼의 표시 여부
    content: (@Composable () -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = KieroTheme.colors.black.copy(alpha = 0.75f))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = KieroTheme.colors.gray900,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .alpha(if (isDisabled) 0f else 1f)
                            .noRippleClickable(onClick = onDismiss)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title.orEmpty(),
                    color = KieroTheme.colors.white,
                    style = KieroTheme.typography.semiBold.title2
                )

                if (content != null) {
                    content()
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = subDescription.orEmpty(),
                    color = KieroTheme.colors.gray100,
                    style = KieroTheme.typography.regular.body3
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (cancelAction != null) {
                        cancelAction(modifier = Modifier.weight(1f))
                    }

                    confirmAction(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun KieroDialogPreview() {
    KieroTheme {
        KieroDialog(
            onDismiss = {},
            title = "제목",
            subDescription = "로그아웃 하시겠습니까?",

            cancelAction = KieroCancelAction(
                text = "취소",
                onClick = {}
            ),
            confirmAction = KieroConfirmAction(
                text = "확인",
                onClick = {}
            ),

            content = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val coinImage = painterResource(R.drawable.img_kid_coin)

                    Image(
                        painter = coinImage,
                        contentDescription = null,
                        modifier = Modifier.forcePixelToDp(coinImage)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "150 개",
                        color = KieroTheme.colors.main,
                        style = KieroTheme.typography.semiBold.title4,
                    )
                }
            }
        )
    }
}