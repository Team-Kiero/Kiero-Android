package com.kiero.presentation.parent.schedule.plan.component.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            )
            .padding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onPreviousClick)
        )

        Spacer(modifier = Modifier.width(44.dp))

        Text(
            text = date,
            style = KieroTheme.typography.semiBold.title4,
            color = KieroTheme.colors.white,
        )

        Spacer(modifier = Modifier.width(44.dp))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onNextClick)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun ScheduleDatebarPreview() {
    KieroTheme {
        ScheduleDatebar(
            date = "12월  2주차",
            onPreviousClick = {},
            onNextClick = {}
        )
    }
}