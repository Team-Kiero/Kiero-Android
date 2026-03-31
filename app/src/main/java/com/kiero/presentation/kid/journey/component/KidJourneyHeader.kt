package com.kiero.presentation.kid.journey.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.component.chip.action.KieroStoneAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderUiModel

@Composable
fun KidJourneyHeader(
    header: KidJourneyHeaderUiModel,
    modifier: Modifier = Modifier,
    isFireLit: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            KidProfileChip(kidName = header.kidName)

            Spacer(modifier = Modifier.weight(1f))

            KieroChip(
                action = KieroCoinAction(
                    coinCount = header.coinCount,
                    onClick = {}
                ),
                modifier = Modifier.padding(top = 5.dp),
                isEnabled = true
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = header.currentDate,
                style = KieroTheme.typography.regular.body3,
                color = KieroTheme.colors.gray500
            )

            Spacer(modifier = Modifier.weight(1f))

            KieroChip(
                action = KieroStoneAction(
                    currentStoneCount = header.earnedStones!!,
                    maxStoneCount = header.totalScheduleCount!!,
                    isFireLit = isFireLit,
                    onClick = {}
                ),
                isEnabled = true,
                isCompleted = isFireLit || header.earnedStones == header.totalScheduleCount
                        && header.totalScheduleCount > 0
            )
        }
    }
}

@Preview
@Composable
private fun KidJourneyHeaderPreview() {
    KieroTheme {
        KidJourneyHeader(
            header = KidJourneyHeaderUiModel(
                kidName = "주완",
                currentDate = "12월 5일 목요일",
                coinCount = 350,
                earnedStones = 7,
                totalScheduleCount = 7
            ),
            isFireLit = true
        )
    }
}
