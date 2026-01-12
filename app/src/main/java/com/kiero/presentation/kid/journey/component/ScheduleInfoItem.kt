package com.kiero.presentation.kid.journey.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.model.JourneyScheduleModel
import java.time.LocalTime

@Composable
fun ScheduleInfoItem(
    item: JourneyScheduleModel,
    modifier: Modifier = Modifier
) {
    val timeAnnotatedString = buildAnnotatedString {
        val amPmStyle = KieroTheme.typography.regular.body5.toSpanStyle().copy(
            baselineShift = BaselineShift(0.05f)
        )

        withStyle(style = amPmStyle) { append(item.startTime.format(item.amPmFormatter)) }
        append("  ${item.startTime.format(item.timeNumberFormatter)}  ~  ")


        withStyle(style = amPmStyle) { append(item.endTime.format(item.amPmFormatter)) }
        append("  ${item.endTime.format(item.timeNumberFormatter)}")
    }

    Row(
        modifier = modifier
            .background(color = KieroTheme.colors.gray900, shape = RoundedCornerShape(15.dp))
            .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.displayTitle,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.gray300,
        )

        Spacer(modifier = Modifier.width(40.dp))


        Text(
            text = timeAnnotatedString,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.main,
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun ScheduleInfoItemPreview() {
    ScheduleInfoItem(
        item = JourneyScheduleModel(
            order = 1,
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(10, 0)
        )
    )
}