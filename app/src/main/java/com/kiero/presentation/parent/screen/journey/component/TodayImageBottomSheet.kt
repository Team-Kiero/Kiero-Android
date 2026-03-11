package com.kiero.presentation.parent.screen.journey.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiero.core.designsystem.component.bottomsheet.KieroBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayJourneyBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    KieroBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier,
    ) {

    }
}

@Composable
fun TodayJourneyContent(
    modifier: Modifier = Modifier
) {

}
