package com.kiero.presentation.parent.schedule.plan.component.picker

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
fun PickerTopbar(
    title: String,
    leftIconRes: Int,
    rightIconRes: Int,
    leftIconClick: () -> Unit,
    rightIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Icon(
            imageVector = ImageVector.vectorResource(id = leftIconRes),
            contentDescription = null,
            tint = KieroTheme.colors.white,
            modifier = Modifier
                .noRippleClickable(onClick = leftIconClick)
        )

        Text(
            text = title,
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = rightIconRes),
            contentDescription = null,
            tint = KieroTheme.colors.white,
            modifier = Modifier
                .noRippleClickable(onClick = rightIconClick)
        )
    }
}

@Preview
@Composable
private fun KieroTopbarReview() {
    KieroTheme {
        PickerTopbar(
            title = "알람",
            leftIconRes = R.drawable.ic_arrow_left,
            rightIconRes = R.drawable.ic_arrow_right,
            leftIconClick = {},
            rightIconClick = {}
        )
    }
}
