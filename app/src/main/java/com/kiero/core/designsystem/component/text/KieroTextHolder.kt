package com.kiero.core.designsystem.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroTextHolder(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = KieroTheme.colors.gray900, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 13.dp, vertical = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.white
        )
    }
}

@Preview
@Composable
private fun KieroTextHolderPreview() {
    KieroTheme {
        KieroTextHolder(
            text = "키어로"
        )
    }
}