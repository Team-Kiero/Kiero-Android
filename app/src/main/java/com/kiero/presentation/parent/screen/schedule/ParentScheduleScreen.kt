package com.kiero.presentation.parent.screen.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.statusBarColor
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.Gray900
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.RefreshState
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.parent.component.ParentTopbar
import com.kiero.presentation.parent.component.PlanTabFab
import com.kiero.presentation.parent.screen.schedule.model.TabItem
import com.kiero.presentation.parent.screen.schedule.plan.ParentPlanScreen
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState
import com.kiero.presentation.parent.screen.schedule.viewmodel.ParentScheduleViewModel
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import java.time.LocalDate

@Composable
fun ParentScheduleRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
    navigateToScheduleAdd: (String, Boolean) -> Unit,
    navigateToAlarm: () -> Unit,
    viewModel: ParentScheduleViewModel = hiltViewModel(),
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val authState by viewModel.authstate.collectAsStateWithLifecycle()
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
    val childId by viewModel.childId.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchSchedule()
        viewModel.ensureChildIdAndStartSse()
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ParentSignUpSideEffect.NavigateToSelection -> navigateToSelection()

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                KieroLoadingIndicator()
            }

            is UiState.Success -> {
                ParentScheduleScreen(
                    paddingValues = paddingValues,
                    scheduleState = state.data,
                    onDateChange = viewModel::onDateChange,
                    onResetToToday = viewModel::resetToday,
                    navigateToScheduleAdd = {
                        navigateToScheduleAdd(
                            state.data.navInitialDate,
                            state.data.isFireLit
                        )
                    },
                    navigateToAlarm = navigateToAlarm,
                )
            }

            is UiState.Failure -> {
                // 에러 시 스낵바나 재시도 버튼 표시
            }

            is UiState.Empty -> {
                ParentScheduleScreen(
                    paddingValues = paddingValues,
                    scheduleState = ParentScheduleState(),
                    onResetToToday = viewModel::resetToday,
                    onDateChange = viewModel::onDateChange,
                    navigateToScheduleAdd = {
                        navigateToScheduleAdd(LocalDate.now().toString(), false)
                    },
                    navigateToAlarm = navigateToAlarm,
                )
            }
        }
    }
}

@Composable
private fun ParentScheduleScreen(
    paddingValues: PaddingValues,
    scheduleState: ParentScheduleState,
    modifier: Modifier = Modifier,
    onResetToToday: () -> Unit,
    onDateChange: (Boolean) -> Unit,
    navigateToScheduleAdd: () -> Unit,
    navigateToAlarm: () -> Unit,
) {
    val tabs = remember { TabItem.entries.map { it.title } }
    var isMissionFabExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(color = KieroTheme.colors.black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarColor(backgroundColor = Gray900)
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ParentPlanScreen(
                state = scheduleState,
                onDateChange = onDateChange,
                onResetToday = onResetToToday,
            )
        }

        if (isMissionFabExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KieroTheme.colors.black.copy(alpha = 0.75f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isMissionFabExpanded = false
                    }
            )
        }

        PlanTabFab(
            onScheduleAdd = { navigateToScheduleAdd() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
@Preview
private fun ParentScheduleScreenPreview() {
    KieroTheme {
        CompositionLocalProvider(
            LocalRefreshState provides RefreshState()
        ) {
            ParentScheduleScreen(
                paddingValues = PaddingValues(),
                scheduleState = ParentScheduleState(),
                onDateChange = {},
                onResetToToday = {},
                navigateToScheduleAdd = {},
                navigateToAlarm = {}
            )
        }

    }
}
