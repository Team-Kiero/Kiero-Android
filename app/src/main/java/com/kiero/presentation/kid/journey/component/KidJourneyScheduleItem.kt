package com.kiero.presentation.kid.journey.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel

@Composable
fun KidJourneyScheduleItem(
    item: KidJourneyScheduleUiModel,
    modifier: Modifier = Modifier
) {
    val formattedStart = item.getFormattedStartTime()
    val formattedEnd = item.getFormattedEndTime()

    val timeAnnotatedString = buildAnnotatedString {
        val amPmStyle = KieroTheme.typography.regular.body5.toSpanStyle().copy(
            baselineShift = BaselineShift(0.05f)
        )

        withStyle(style = amPmStyle) {
            append(formattedStart.substring(0, 2))
        }
        append(formattedStart.substring(2))

        append("  ~  ")

        withStyle(style = amPmStyle) {
            append(formattedEnd.substring(0, 2))
        }
        append(formattedEnd.substring(2))
    }

    Row(
        modifier = modifier
            .background(color = KieroTheme.colors.gray900, shape = RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.displayTitle,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.gray300,
        )

        Spacer(modifier = Modifier.weight(1f))


        Text(
            text = timeAnnotatedString,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.main,
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun KidJourneyScheduleItemPreview() {
    KidJourneyScheduleItem(
        item = KidJourneyScheduleUiModel(
            order = 1,
            startTime = "14:00:00",
            endTime = "16:00:00"
        )
    )
}