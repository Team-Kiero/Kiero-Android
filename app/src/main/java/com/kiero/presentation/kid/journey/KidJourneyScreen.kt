package com.kiero.presentation.kid.journey

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.viewmodel.KidJourneyViewModel

@Composable
fun KidJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidJourneyViewModel = hiltViewModel()
) {
    val globalTrigger = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is KidJourneySideEffect.ShowToast -> globalTrigger.showToast(sideEffect.message)
            is KidJourneySideEffect.ShowSnackbar -> globalTrigger.showSnackbar(
                sideEffect.message,
                null
            ) {}

            KidJourneySideEffect.ShowDialog -> globalTrigger.dialogTrigger.show {}
        }
    }

    KidJourneyScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidJourneyScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            text = "오늘의 여정"
        )
    }
}

@Composable
@Preview
private fun KidJourneyScreenPreview() {
    KieroTheme {}
}
