package com.kiero.presentation.parent.mission.component.datepicker.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarMonthHeader(
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit,
    yearMonth: YearMonth,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.gray900,
            )
            .padding(vertical = 10.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = KieroTheme.colors.white,
            modifier = Modifier.noRippleClickable(onClick = onLeftArrowClick)
        )

        Text(
            text = "${yearMonth.year}년 ${
                yearMonth.month.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                )
            }",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.regular.body2
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = KieroTheme.colors.white,
            modifier = Modifier.noRippleClickable(onClick = onRightArrowClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarMonthHeaderPreview() {
    KieroTheme {
        CalendarMonthHeader(
            onLeftArrowClick = {},
            onRightArrowClick = {},
            modifier = Modifier,
            yearMonth = YearMonth.now()
        )
    }
}