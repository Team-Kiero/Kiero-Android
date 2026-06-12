package com.kiero.presentation.kid.component

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
fun KidHeader(
    count: Int,
    modifier: Modifier = Modifier,
    isSchedule: Boolean = true,
) {
    val headerText = if (isSchedule) {
        "오늘은 ${count}개의 여정이 있어!"
    }else{
        "지금까지 ${count}번의 소원을 빌었어!"
    }
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
            text = headerText,
            color = KieroTheme.colors.gray200,
            style = KieroTheme.typography.regular.body4
        )
    }
}
