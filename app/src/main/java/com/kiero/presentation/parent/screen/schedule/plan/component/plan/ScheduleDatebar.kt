package com.kiero.presentation.parent.screen.schedule.plan.component.plan

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ScheduleDatebar(
    date: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviousEnabled: Boolean = true,
    isNextEnabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(horizontal = 50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = if (isPreviousEnabled) {
                KieroTheme.colors.white
            } else {
                KieroTheme.colors.gray600
            },
            modifier = Modifier.noRippleClickable(
                onClick = {
                    if (isPreviousEnabled) onPreviousClick()
                }
            )
        )

        Text(
            text = date,
            style = KieroTheme.typography.semiBold.title4,
            color = KieroTheme.colors.white,
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = if (isNextEnabled) {
                KieroTheme.colors.white
            } else {
                KieroTheme.colors.gray600
            },
            modifier = Modifier.noRippleClickable(
                onClick = {
                    if (isNextEnabled) onNextClick()
                }
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun ScheduleDatebarPreview() {
    KieroTheme {
        ScheduleDatebar(
            date = "12월 2주차",
            onPreviousClick = {},
            onNextClick = {},
            isPreviousEnabled = false,
            isNextEnabled = false
        )
    }
}