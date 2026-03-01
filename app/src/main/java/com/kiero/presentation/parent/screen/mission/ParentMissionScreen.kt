package com.kiero.presentation.parent.screen.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.kiero.core.common.extension.toRelativeDayFromDate
import com.kiero.core.common.util.ParentFormatters
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.parent.screen.mission.component.missionmain.MissionInfo
import com.kiero.presentation.parent.screen.mission.component.missionmain.MissionListItem
import com.kiero.presentation.parent.screen.mission.state.ParentMissionState
import com.kiero.presentation.parent.screen.mission.viewmodel.ParentMissionViewModel

@Composable
fun ParentMissionRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentMissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMissions()
    }

    when (val uiState = state) {
        is UiState.Success -> {
            ParentMissionScreen(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
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
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
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
        contentPadding = PaddingValues(16.dp)
    ) {
        state.kidMissionByDateList.missionsByDate.forEach { missionsByDate ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = KieroTheme.colors.black)
                        .padding(vertical = 8.dp)
                ) {
                    MissionInfo(
                        dayOfWeek = missionsByDate.dueAt.toRelativeDayFromDate,
                        dueAt = ParentFormatters.formatDateWithDayOfWeek(missionsByDate.dueAt)
                    )
                }
            }
            items(
                items = missionsByDate.missions,
            ) { mission ->
                Box(modifier = Modifier.padding(vertical = 6.dp)) {
                    MissionListItem(
                        missionTitle = mission.name,
                        reward = mission.reward
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ParentMissionScreenPreview() {
    KieroTheme {
        ParentMissionRoute(
            paddingValues = PaddingValues(),
            navigateUp = {}
        )
    }
}