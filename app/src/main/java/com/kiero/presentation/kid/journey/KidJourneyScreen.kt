package com.kiero.presentation.kid.journey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.component.chip.action.KieroStoneAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.component.SpeechField
import com.kiero.presentation.kid.journey.component.ScheduleInfoItem
import com.kiero.presentation.kid.journey.model.JourneyScheduleModel
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.state.KidJourneyState
import com.kiero.presentation.kid.journey.state.ScheduleStatus
import com.kiero.presentation.kid.journey.viewmodel.KidJourneyViewModel
import java.time.LocalTime

@Composable
fun KidJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidJourneyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is KidJourneySideEffect.ShowToast -> globalTrigger.showToast(sideEffect.message)
            is KidJourneySideEffect.ShowSnackbar -> globalTrigger.showSnackbar(
                SnackbarState(message = sideEffect.message)
            )
            KidJourneySideEffect.ShowDialog -> globalTrigger.dialogTrigger.show {}
        }
    }

    KidJourneyScreen(
        paddingValues = paddingValues,
        state = state,
        onButtonClick = { },
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidJourneyScreen(
    paddingValues: PaddingValues,
    state: KidJourneyState,
    onButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_kid_journey_mask_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 132.dp)
                .fillMaxWidth()
                .forcePixelToDp(painterResource(id = R.drawable.img_kid_journey_mask_background))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp, bottom = 6.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    KidProfileChip(kidName = state.kidName)
                    Text(
                        text = "12월 5일 목요일", // TODO: 실제 날짜 포맷터 사용
                        style = KieroTheme.typography.regular.body3,
                        color = KieroTheme.colors.gray500
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    KieroChip(
                        action = KieroCoinAction(
                            coinCount = 350,
                            onClick = {}
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    KieroChip(
                        action = KieroStoneAction(
                            currentStoneCount = state.earnedStones,
                            maxStoneCount = state.totalScheduleCount,
                            onClick = {}
                        ),
                        isEnabled = true,
                        isCompleted = state.earnedStones == state.totalScheduleCount
                                && state.totalScheduleCount > 0
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 스케줄 정보 - 특정 상태에서만 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (shouldShowSchedule(state.scheduleStatus)) 1f else 0f)
            ) {
                state.currentSchedule?.let { schedule ->
                    ScheduleInfoItem(item = schedule)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 꾸비 메시지
            SpeechField(
                name = "꾸비",
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        id = state.goblinMessageRes,
                        *getMessageArgs(state)
                    ),
                    color = KieroTheme.colors.gray300,
                    style = KieroTheme.typography.regular.body3
                )
            }

            Spacer(modifier = Modifier.height(11.dp))

            // 버튼 - 특정 상태에서만 표시
            state.buttonTextRes?.let { buttonTextRes ->
                val isAuthButton = buttonTextRes == R.string.journey_btn_auth

                KieroButtonMedium(
                    text = stringResource(buttonTextRes),
                    leadingIcon = if (isAuthButton) {
                        ImageVector.vectorResource(id = R.drawable.ic_kid_camera)
                    } else null,
                    containerColor = KieroTheme.colors.gray900,
                    contentColor = KieroTheme.colors.white,
                    onClick = { }
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.img_kid_journey_goblin),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp)
        )
    }
}

// 스케줄 표시 여부 결정
private fun shouldShowSchedule(status: ScheduleStatus): Boolean {
    return when (status) {
        ScheduleStatus.FIRST_SCHEDULE,
        ScheduleStatus.NOW_SCHEDULE_EXIST,
        ScheduleStatus.NEXT_SCHEDULE_EXIST -> true
        ScheduleStatus.NO_SCHEDULE,
        ScheduleStatus.FIRE_NOT_LIT,
        ScheduleStatus.FIRE_LIT -> false
    }
}

// 메시지에 들어갈 인자 생성
private fun getMessageArgs(state: KidJourneyState): Array<Any> {
    return when (state.scheduleStatus) {
        ScheduleStatus.FIRST_SCHEDULE -> {
            arrayOf(state.currentSchedule?.displayTitle ?: "")
        }
        ScheduleStatus.NOW_SCHEDULE_EXIST -> {
            arrayOf(
                state.currentSchedule?.displayTitle ?: "",
                "불조각"
            )
        }
        ScheduleStatus.NEXT_SCHEDULE_EXIST -> {
            arrayOf(
                state.currentSchedule?.displayTitle ?: "",
                "불조각"
            )
        }
        ScheduleStatus.FIRE_NOT_LIT -> {
            arrayOf(
                state.kidName,
                "영웅의 불꽃"
            )
        }
        ScheduleStatus.NO_SCHEDULE,
        ScheduleStatus.FIRE_LIT -> {
            // 인자 없음
            emptyArray()
        }
    }
}

@Composable
@Preview
private fun KidJourneyScreenPreview() {
    KieroTheme {
        KidJourneyScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = KidJourneyState(
                kidName = "주완",
                scheduleStatus = ScheduleStatus.FIRST_SCHEDULE,
                currentSchedule = JourneyScheduleModel(
                    order = 1,
                    startTime = LocalTime.of(9, 0),
                    endTime = LocalTime.of(10, 0)
                ),
                totalScheduleCount = 7,
                earnedStones = 1
            ),
            onButtonClick = {}
        )
    }
}