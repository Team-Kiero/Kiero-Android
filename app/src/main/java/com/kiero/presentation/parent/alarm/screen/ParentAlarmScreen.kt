package com.kiero.presentation.parent.alarm.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAlarmRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    ParentAlarmScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp
    )
}

@Composable
private fun ParentAlarmScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Text(
            text = "알람 피드"
        )
    }
}

@Composable
@Preview
private fun ParentAlarmRoutePreview() {
    KieroTheme {}
}