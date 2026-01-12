package com.kiero.presentation.kid.mission.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidWishMissionItem(
    missionTitle: String,
    missionReward: Int,
    isCompleted: Boolean,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completeTitleColor = if (isCompleted) KieroTheme.colors.gray500 else KieroTheme.colors.white
    val completeRewardColor =
        if (isCompleted) KieroTheme.colors.gray600 else KieroTheme.colors.gray400
    val completeAlpha = if (isCompleted) 0.6f else 1f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.gray900.copy(alpha = completeAlpha),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 15.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "금화 $missionReward 개",
                style = KieroTheme.typography.regular.body4,
                color = completeRewardColor
            )

            Text(
                text = missionTitle,
                style = KieroTheme.typography.regular.body2,
                color = completeTitleColor
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!isCompleted) {
            Box(
                modifier = Modifier
                    .background(
                        color = KieroTheme.colors.white,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(
                        horizontal = 22.dp,
                        vertical = 10.dp
                    )
                    .noRippleClickable(
                        onClick = onClickButton
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "버튼",
                    color = KieroTheme.colors.black,
                    style = KieroTheme.typography.semiBold.title4
                )
            }
        } else {
            Text(
                text = "성공!",
                color = KieroTheme.colors.gray500,
                style = KieroTheme.typography.semiBold.title4
            )
        }
    }
}

@Preview
@Composable
private fun KidWishMissionItemPreview() {
    KieroTheme {
        KidWishMissionItem(
            missionTitle = "미션 이름",
            missionReward = 2,
            isCompleted = true,
            onClickButton = {}
        )
    }
}