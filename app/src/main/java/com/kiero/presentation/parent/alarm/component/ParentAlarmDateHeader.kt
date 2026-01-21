package com.kiero.presentation.parent.alarm.component


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAlarmDateHeader(
    date: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = date,
        style = KieroTheme.typography.semiBold.title3,
        color = KieroTheme.colors.gray200,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun ParentAlarmDataHeaderPreview()
{
    KieroTheme{
        ParentAlarmDateHeader(date = "2025.12.26.(금)")
    }
}