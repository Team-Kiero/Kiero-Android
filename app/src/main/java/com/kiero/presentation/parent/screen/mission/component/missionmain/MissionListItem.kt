package com.kiero.presentation.parent.screen.mission.component.missionmain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun MissionListItem(
    missionTitle: String,
    reward: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = missionTitle,
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.white,
            modifier = Modifier.weight(1f),
            maxLines = 1,
        )

        Spacer(modifier = Modifier.width(16.dp))

        /*
            TODO : pull 받고 수정
         */
        KieroChip(
            action = KieroCoinAction(
                coinCount = reward,
                isEnabled = true,
                onClick = {}
            )
        )
    }
}

@Composable
fun MissionInfo(
    dueAt: String,
    modifier: Modifier = Modifier,
    dayOfWeek: String? = null,
    isVisible: Boolean = true,
) {
    val annotateDate = remember(dueAt) {
        buildAnnotatedString {
            val splitIndex = dueAt.lastIndexOf(".(")
            if (splitIndex != -1) {
                append(dueAt.substring(0, splitIndex))

                withStyle(style = SpanStyle()) {
                    append(dueAt.substring(splitIndex))
                }
            } else {
                append(dueAt)
            }
        }
    }

    val dayTextColor = if (dayOfWeek == "오늘") {
        KieroTheme.colors.main
    } else {
        KieroTheme.colors.schedule1
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Unspecified)
            .alpha(if (isVisible) 1f else 0f),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = dayOfWeek ?: " ",
            color = dayTextColor,
            style = KieroTheme.typography.semiBold.title4
        )

        Text(
            text = annotateDate,
            color = KieroTheme.colors.gray200,
            style = KieroTheme.typography.semiBold.title3
        )
    }
}

@Preview
@Composable
private fun MissionListItemPreview() {
    KieroTheme {
        Column {
            MissionListItem(
                missionTitle = "테스트 미션 테스트 미션 테스트 미션 테스트 미션 테스트미션 미션미션",
                reward = 150,
            )

            MissionInfo(
                dayOfWeek = "월요일",
                dueAt = "2023.05.01"
            )
        }
    }
}
