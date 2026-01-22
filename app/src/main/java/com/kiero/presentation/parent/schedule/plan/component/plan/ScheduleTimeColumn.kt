package com.kiero.presentation.parent.schedule.plan.component.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ScheduleTimeColumn(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(23.dp)
            .background(
                color = Color.Unspecified
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "8",
            color = KieroTheme.colors.gray600,
            style = KieroTheme.typography.regular.body5,
            modifier = Modifier
                .padding(bottom = 27.dp)
        )
        (9..21).forEach { hour ->
            Box(
                modifier = Modifier
                    .height(38.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "$hour",
                    color = KieroTheme.colors.gray600,
                    style = KieroTheme.typography.regular.body5,
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 7.dp, start = 5.dp, end = 5.dp)
                )

                HorizontalDivider(
                    thickness = 0.3.dp,
                    color = KieroTheme.colors.gray800
                )
            }
        }
    }
}


@Preview(name = "시간 컬럼", showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun TimeColumnPreview() {
    ScheduleTimeColumn()
}

