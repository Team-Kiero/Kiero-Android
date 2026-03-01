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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentContentBottomSheet(
    topTitle: String,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    cotent: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = KieroTheme.colors.gray900)
                .padding(16.dp)
        ) {
            BottomSheetTopbar(
                title = topTitle,
                onCancelClick = onDismissRequest
            )

            cotent()

            Spacer(modifier = Modifier.height(35.dp))

            BottomSheetActionArea(
                actionTitle = "수정하기",
                onActionClick = onEditClick,
                actionIcon = R.drawable.ic_parent_content_edit
            )

            BottomSheetActionArea(
                actionTitle = "삭제하기",
                onActionClick = onDeleteClick,
                actionIcon = R.drawable.ic_parent_content_delete
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
    onActionClick: () -> Unit,
    @DrawableRes actionIcon: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .noRippleClickable(onClick = onActionClick),
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
private fun PreviewParentContentBottomSheet() {
    KieroTheme {
        ParentContentBottomSheet(
            topTitle = "피아노 학원",
            onDismissRequest = {},
            onEditClick = {},
            onDeleteClick = {},
            cotent = {
                Text(
                    text = "테스트 텍스트",
                    color = KieroTheme.colors.white
                )
            }
        )
    }
}