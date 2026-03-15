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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.util.toDotSeparatedDate
import com.kiero.core.designsystem.component.emptyview.KieroEmptyView
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.screen.journey.component.ParentJourneyBottomSheet
import com.kiero.presentation.parent.screen.journey.component.ParentJourneyTodayKidInfo
import com.kiero.presentation.parent.screen.journey.component.ParentJourneyTodayMissionStatus
import com.kiero.presentation.parent.screen.journey.component.ParentJourneyTodayStatusItem
import com.kiero.presentation.parent.screen.journey.component.TodayJourneyDialog
import com.kiero.presentation.parent.screen.journey.model.KidInfo
import com.kiero.presentation.parent.screen.journey.model.TodayJourneyUiModel
import java.time.LocalDate

@Composable
fun ParentJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentJourneyViewModel = hiltViewModel()
) {
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            ParentJourneySideEffect.NavigateUp -> navigateUp()
            is ParentJourneySideEffect.ShowSnackbar -> globalUiEventHolder.showSnackbar(
                SnackbarState(
                    message = it.message
                )
            )
        }
    }

    LaunchedEffect(state.kidInfo.kidId) {
        if (state.kidInfo.kidId.isNotEmpty()) {
            viewModel.fetchParentJourney(state.kidInfo.kidId.toLong())
        }
    }

    ParentJourneyScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        onClickJourneyItem = viewModel::fetchScheduleImage,
        state = state
    )
}

@Composable
private fun ParentJourneyScreen(
    paddingValues: PaddingValues,
    state: ParentJourneyState,
    navigateUp: () -> Unit,
    onClickJourneyItem: (Long) -> Unit = {}
) {
    var initialTab by remember { mutableIntStateOf(0) }

    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var isTodayJourneyVisible by remember { mutableStateOf(false) }

    var selectedJourneyItem by remember { mutableStateOf<TodayJourneyUiModel?>(null) }


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
                    completeMissions = state.completeMissions,
                    incompleteMissions = state.incompleteMissions,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onClick = {
                        initialTab = if (it) 0 else 1
                        isBottomSheetVisible = true
                    }
                )
            }
        }

        if (state.todayMissionList.isEmpty()) {
            KieroEmptyView(
                description = "일정을 등록해주세요."
            )
        } else {
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp, horizontal = 24.dp),
            ) {
                itemsIndexed(
                    items = state.todayMissionList,
                ) { index, item ->
                    ParentJourneyTodayStatusItem(
                        item = item,
                        onItemClick = {
                            selectedJourneyItem = it
                            onClickJourneyItem(it.scheduleDetailId)
                            isTodayJourneyVisible = true
                        }
                    )
                }
            }
        }
    }

    if (isBottomSheetVisible) {
        ParentJourneyBottomSheet(
            completeMissions = state.completeMissions,
            incompleteMissions = state.incompleteMissions,
            initialTab = initialTab,
            onDismiss = { isBottomSheetVisible = false }
        )
    }

    if (isTodayJourneyVisible) {
        TodayJourneyDialog(
            missionTitle = selectedJourneyItem?.todayMission.orEmpty(),
            imageUrl = state.selectedJourneyImageUrl,
            onDismiss = {
                isTodayJourneyVisible = false
            }
        )
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
                    currentDate = LocalDate.now().toDotSeparatedDate()
                ),
            )
        )
    }
}
