package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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

@Composable
fun ParentAutoMissionCalendar(
    dateText: String,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = KieroTheme.colors.gray900)
            .padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "마감일",
            color = KieroTheme.colors.gray200,
            style = KieroTheme.typography.regular.body3
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .noRippleClickable(onClick = onDateClick),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = dateText,
                color = KieroTheme.colors.gray200,
                style = KieroTheme.typography.semiBold.title3
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_mission_date),
                contentDescription = null,
                tint = Color.Unspecified
            )

        }

    }
}

@Preview
@Composable
private fun ParentMissionCalendarPreview() {
    KieroTheme {
        ParentAutoMissionCalendar(
            dateText = "2025.12.16",
            onDateClick = {}
        )
    }
}