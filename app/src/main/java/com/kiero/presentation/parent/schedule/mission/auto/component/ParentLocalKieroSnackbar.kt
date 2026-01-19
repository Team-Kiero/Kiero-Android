package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentLocalKieroSnackbar(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(KieroTheme.colors.schedule1)
            .padding(15.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray900,
            textAlign = TextAlign.Center
        )
    }
}