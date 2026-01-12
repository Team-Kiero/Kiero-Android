package com.kiero.presentation.kid.mission.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMissionTitle(
    title: String,
    subTitle: String,
    modifier : Modifier = Modifier
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = KieroTheme.typography.semiBold.title4,
            color = KieroTheme.colors.gray200,
            textAlign = TextAlign.Start
        )

        Text(
            text = subTitle,
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray200,
            textAlign = TextAlign.Start
        )
    }
}

@Preview
@Composable
private fun KidWishMissionTitlePreview() {
    KieroTheme {
        KidMissionTitle(
            title = "미션마감",
            subTitle = "오늘까지"
        )
    }
}