package com.kiero.presentation.kid.journey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.journey.component.KidJourneyActionButton
import com.kiero.presentation.kid.journey.component.KidJourneyGoblinMessage
import com.kiero.presentation.kid.journey.component.KidJourneyHeader
import com.kiero.presentation.kid.journey.component.KidJourneyNextButton
import com.kiero.presentation.kid.journey.component.KidJourneyScheduleItem
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleModel
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.state.KidJourneyState
import com.kiero.presentation.kid.journey.util.KidJourneyContentUtil
import com.kiero.presentation.kid.journey.viewmodel.KidJourneyViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun KidJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCamera: () -> Unit,
    navigateToFire: () -> Unit,
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
        onButtonClick = {
            when (state.buttonType) {
                KidJourneyButtonType.AUTH -> navigateToCamera()
                KidJourneyButtonType.FIRE -> navigateToFire()
                else -> { /* NONE 인 경우 처리 */ }
            }
        },
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
        // 배경 이미지
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
            // 헤더
            state.header?.let { header ->
                KidJourneyHeader(header = header)
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 스케줄 정보 (특정 상태에서만 표시)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (state.shouldShowSchedule) 1f else 0f)
            ) {
                KidJourneyContentUtil.getScheduleInfo(state.content)?.let { schedule ->
                    KidJourneyScheduleItem(item = schedule)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            KidJourneyNextButton(
                isVisible = state.shouldShowNextButton,
                modifier = Modifier
                    .align(Alignment.End),
                onClick = {  }
            )

            // 꾸비 메시지
            KidSpeechField(
                name = "꾸비",
                modifier = Modifier.fillMaxWidth()
            ) {
                KidJourneyGoblinMessage(
                    content = state.content,
                    messageRes = state.goblinMessageRes,
                    messageArgs = state.getMessageArgs()
                )
            }

            Spacer(modifier = Modifier.height(11.dp))

            // 액션 버튼 (특정 상태에서만 표시)
            KidJourneyActionButton(
                buttonType = state.buttonType,
                buttonTextRes = state.buttonTextRes,
                onClick = onButtonClick
            )
        }

        // 꾸비 이미지
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

@Composable
@Preview
private fun KidJourneyScreenPreview() {
    KieroTheme {
        KidJourneyScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = KidJourneyState(
                header = KidJourneyHeaderModel(
                    kidName = "주완",
                    currentDate = LocalDate.of(2024, 12, 5),
                    coinCount = 350,
                    earnedStones = 5,
                    totalScheduleCount = 7
                ),
                content = KidJourneyContentModel.NowSchedule(
                    scheduleName = "피아노 학원 가기",
                    stoneType = "용기의 불조각",
                    scheduleInfo = KidJourneyScheduleModel(
                        order = 4,
                        startTime = LocalTime.of(14, 0),
                        endTime = LocalTime.of(16, 0)
                    ),
                    isSkippable = true
                )
            ),
            onButtonClick = {}
        )
    }
}