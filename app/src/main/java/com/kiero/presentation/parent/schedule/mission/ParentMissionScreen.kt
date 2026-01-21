package com.kiero.presentation.parent.schedule.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.parent.schedule.mission.state.ParentMissionState
import com.kiero.presentation.parent.schedule.mission.viewmodel.ParentMissionViewModel

@Composable
fun ParentMissionRoute(
    paddingValues: PaddingValues,
    viewModel: ParentMissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMissions()
    }

    when (val uiState = state) {
        is UiState.Success -> {
            ParentMissionScreen(
                state = uiState.data,
            )
        }

        is UiState.Loading -> {
            CircularProgressIndicator()
        }

        is UiState.Failure -> {}

        UiState.Empty -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_mission_empty_view),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun ParentMissionScreen(
    state: ParentMissionState,
    modifier: Modifier = Modifier,
) {
    val refreshState = LocalRefreshState.current
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        refreshState.refreshEvent.collect { tab ->
            if (tab == ParentMainTab.SCHEDULE) {
                listState.animateScrollToItem(0)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = state.kidMissionByDateList.missionsByDate,
            key = { missionsByDate -> missionsByDate.dueAt }
        ) { missionsByDate ->
            MissionDateGroup(
                missionsByDate = missionsByDate,
            )
        }
    }
}

@Preview
@Composable
private fun ParentMissionScreenPreview() {
    KieroTheme {
        ParentMissionRoute(
            paddingValues = PaddingValues()
        )
    }
}