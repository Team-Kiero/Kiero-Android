package com.kiero.presentation.kid.journey.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMapHeader(
    scheduleCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 12.dp, horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "오늘은 ${scheduleCount}개의 여정이 있어!",
            color = KieroTheme.colors.gray200,
            style = KieroTheme.typography.regular.body4
        )
    }
}