package com.kiero.presentation.parent.screen.journey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.journey.component.ParentJourneyTodayKidInfo
import com.kiero.presentation.parent.screen.journey.component.ParentJourneyTodayMissionStatus
import com.kiero.presentation.parent.screen.journey.component.ParentJourneyTodayStatusItem
import com.kiero.presentation.parent.screen.journey.model.KidInfo

@Composable
fun ParentJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentJourneyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            ParentJourneySideEffect.NavigateUp -> navigateUp()
        }
    }

    ParentJourneyScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        state = state
    )
}

@Composable
private fun ParentJourneyScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    state: ParentJourneyState,
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KieroTheme.colors.gray900)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                ParentJourneyTodayKidInfo(kidInfo = state.kidInfo)
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.matchParentSize()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth().background(KieroTheme.colors.gray900))
                    Box(modifier = Modifier.weight(1f).fillMaxWidth().background(KieroTheme.colors.black))
                }

                ParentJourneyTodayMissionStatus(
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        // Todo: 서버 내용으로 수정
        LazyColumn (
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp, horizontal = 24.dp),
        ) {
            itemsIndexed(
                items = ParentJourneyState.FAKE
            ) { index, item ->
                ParentJourneyTodayStatusItem(
                    item = item,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ParentJourneyScreenPreview() {
    KieroTheme {
        ParentJourneyScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = ParentJourneyState(
                kidInfo = KidInfo(
                    kidId = "1",
                    kidName = "민성",
                    currentDate = "2023-09-01"
                ),
            )
        )
    }
}
