package com.kiero.presentation.parent.screen.reward.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.viewtype.DisplayType
import com.kiero.presentation.parent.screen.reward.model.ParentRewardUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentRewardBottomSheet(
    reward: ParentRewardUiModel,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = KieroTheme.colors.gray900,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = reward.name,
                    style = KieroTheme.typography.bold.headLine3,
                    color = KieroTheme.colors.white,
                    modifier = Modifier.padding(vertical = 11.dp)
                )

                KieroChip(
                    isEnabled = false,
                    isCompleted = true,

                    action = KieroCoinAction(
                        coinCount = reward.price,
                        isCompleted = true,
                        isEnabled = false,
                        viewType = DisplayType.KID,
                        onClick = {}
                    )
                )
            }

            Spacer(modifier = Modifier.height(54.dp))

            ParentRewardActionItem(
                iconResId = R.drawable.ic_parent_reward_modify,
                text = "수정하기",
                onClick = onEditClick
            )
            ParentRewardActionItem(
                iconResId = R.drawable.ic_parent_reward_delete,
                text = "삭제하기",
                onClick = onDeleteClick
            )
        }
    }
}
@Composable
private fun ParentRewardActionItem(
    iconResId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = null,
            tint = KieroTheme.colors.white,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.white,
        )
    }
}
@Preview
@Composable
private fun ParentRewardBottomSheetPreview() {
    KieroTheme {
        ParentRewardBottomSheet(
            reward = ParentRewardUiModel(
                couponId = 1L,
                name = "용돈 5000원 받기",
                price = 350,
            ),
            onDismissRequest = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}