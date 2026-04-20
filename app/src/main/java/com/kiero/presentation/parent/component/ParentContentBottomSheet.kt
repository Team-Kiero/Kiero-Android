package com.kiero.presentation.parent.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.disableUpScroll
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentContentBottomSheet(
    topTitle: String,
    onDismissRequest: () -> Unit,
    onEditClick: (() -> Unit)?,
    onDeleteClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
        modifier = modifier,
        containerColor = KieroTheme.colors.gray900,
        contentColor = KieroTheme.colors.white,
        scrimColor = Color.Black.copy(alpha = 0.6f),
    ) {
        Column(
            modifier = Modifier
                .disableUpScroll()
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .background(color = KieroTheme.colors.gray900)
                .padding(16.dp)
        ) {
            BottomSheetTopbar(
                title = topTitle,
                onCancelClick = onDismissRequest
            )

            content()

            // 수정/삭제 영역은 항상 같은 높이만큼 차지
            Spacer(modifier = Modifier.height(35.dp))

            BottomSheetActionArea(
                actionTitle = "수정하기",
                onActionClick = onEditClick,
                actionIcon = R.drawable.ic_parent_content_edit,
                isVisible = onEditClick != null
            )

            Spacer(modifier = Modifier.height(12.dp))

            BottomSheetActionArea(
                actionTitle = "삭제하기",
                onActionClick = onDeleteClick,
                actionIcon = R.drawable.ic_parent_content_delete,
                isVisible = onDeleteClick != null
            )
        }
    }
}

@Composable
private fun BottomSheetTopbar(
    title: String,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp)
    ) {
        Text(
            text = title,
            style = KieroTheme.typography.bold.headLine3,
            color = KieroTheme.colors.white,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable(onClick = onCancelClick)
        )
    }
}

@Composable
private fun BottomSheetActionArea(
    actionTitle: String,
    onActionClick: (() -> Unit)?,
    @DrawableRes actionIcon: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .alpha(if (isVisible) 1f else 0f)
            .noRippleClickable(
                onClick = {
                    if (isVisible) {
                        onActionClick?.invoke()
                    }
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = actionIcon),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Text(
            text = actionTitle,
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.white
        )
    }
}

@Preview
@Composable
private fun PreviewParentContentBottomSheetFull() {
    KieroTheme {
        ParentContentBottomSheet(
            topTitle = "피아노 학원",
            onDismissRequest = {},
            onEditClick = {},
            onDeleteClick = {},
            content = {
                Text(text = "테스트 텍스트", color = KieroTheme.colors.white)
            }
        )
    }
}

@Preview
@Composable
private fun PreviewParentContentBottomSheetReadOnly() {
    KieroTheme {
        ParentContentBottomSheet(
            topTitle = "피아노 학원",
            onDismissRequest = {},
            onEditClick = null,
            onDeleteClick = null,
            content = {
                Text(text = "이미 시작된 일정 (수정/삭제 불가)", color = KieroTheme.colors.white)
            }
        )
    }
}