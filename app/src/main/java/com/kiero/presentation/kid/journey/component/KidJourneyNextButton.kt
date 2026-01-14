package com.kiero.presentation.kid.journey.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidJourneyNextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .padding(top = 5.dp, bottom = 6.dp, start = 11.5.dp, end = 8.5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.journey_btn_next),
            style = KieroTheme.typography.regular.body5,
            color = KieroTheme.colors.gray600
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = KieroTheme.colors.gray600,
            modifier = Modifier
                .size(14.dp)
        )
    }
}

@Preview(showBackground = false)
@Composable
fun KidJourneyNextButtonPreview() {
    KidJourneyNextButton(onClick = {})
}