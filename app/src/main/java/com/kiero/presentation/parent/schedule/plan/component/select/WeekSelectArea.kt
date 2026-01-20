package com.kiero.presentation.parent.schedule.plan.component.select

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WeekSelectArea(
    selectedDays: Set<Int>,
    isRecurring: Boolean,
    onDayClick: (Int) -> Unit,
    onAllDaysSelect: (Boolean) -> Unit,
    onRecurringToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            )
            .padding(
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        DaySection(
            onValidClick = onRecurringToggle,
            isEnabled = isRecurring
        )

        DayPickSection(
            selectedDays = selectedDays,
            onDayClick = onDayClick
        )
        RepeatSection(
            onAbleClick = { isEnabled ->
                onAllDaysSelect(isEnabled)
            },
            isEnabled = selectedDays.size == 7
        )

    }

}

@Composable
private fun DayBox(
    isSelectClick: () -> Unit,
    dayTitle: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
) {
    val (borderColor, textColor) = when (isEnabled) {
        true -> KieroTheme.colors.main to KieroTheme.colors.main
        else -> Color.Unspecified to KieroTheme.colors.gray700
    }
    Text(
        text = dayTitle,
        color = textColor,
        style = KieroTheme.typography.bold.headLine1,
        modifier = modifier
            .noRippleClickable(onClick = isSelectClick)
            .background(
                color = KieroTheme.colors.gray900,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun DayPickSection(
    selectedDays: Set<Int>,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    dayList: ImmutableList<String> = persistentListOf("월", "화", "수", "목", "금", "토", "일"),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            )
            .padding(horizontal = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dayList.forEachIndexed { index, day ->
            DayBox(
                isSelectClick = { onDayClick(index) },
                dayTitle = day,
                modifier = Modifier.weight(1f),
                isEnabled = selectedDays.contains(index)
            )
        }
    }
}


@Composable
private fun RepeatSection(
    onAbleClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
) {
    val iconRes =
        if (isEnabled) R.drawable.ic_parent_addschedule_check_on
        else R.drawable.ic_parent_addschedule_check_off

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable(onClick = { onAbleClick(isEnabled) })
        )

        Text(
            text = "매일",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.regular.body5
        )
    }
}


@Composable
private fun DaySection(
    onValidClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "요일",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "매주 반복",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.bold.headLine4
        )

        DayToggle(
            onClick = onValidClick,
            isChecked = isEnabled
        )
    }
}

@Composable
private fun DayToggle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
) {
    Switch(
        checked = isChecked,
        onCheckedChange = { onClick() },
        modifier = modifier
            .scale(0.7f),
        colors = SwitchDefaults.colors(
            uncheckedThumbColor = KieroTheme.colors.gray200,
            checkedThumbColor = KieroTheme.colors.white,
            checkedTrackColor = KieroTheme.colors.main,
            uncheckedTrackColor = KieroTheme.colors.gray800,
            checkedBorderColor = Color.Unspecified,
            uncheckedBorderColor = Color.Unspecified,
        ),
        thumbContent = {
            Box(
                modifier = Modifier
                    .size(13.dp)
            )
        }

    )
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun PreviewDayBox() {
    KieroTheme {
        Column {
//            WeekSelectArea(
//
//            )
        }
    }
}