package com.kiero.presentation.parent.schedule.plan.component.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.plan.model.DayList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ScheduleWeekTopbar(
    dayList: ImmutableList<DayList>,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            )
            .padding(start = 40.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dayList.forEachIndexed { index, date ->
            Text(
                text = date.day,
                color = if (index == 0) KieroTheme.colors.main else KieroTheme.colors.gray100,
                style = KieroTheme.typography.regular.body5,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun WeekTopbarPreview() {
    KieroTheme {
        ScheduleWeekTopbar(
            dayList = listOf(
                DayList("8(월)"),
                DayList("9(화)"),
                DayList("10(수)"),
                DayList("11(목)"),
                DayList("12(금)"),
                DayList("13(토)"),
                DayList("14(일)")
            ).toPersistentList()
        )
    }
}