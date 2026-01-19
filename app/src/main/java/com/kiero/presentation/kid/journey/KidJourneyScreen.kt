package com.kiero.presentation.kid.journey

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.KieroGifImage
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.journey.component.KidJourneyActionButton
import com.kiero.presentation.kid.journey.component.KidJourneyGoblinMessage
import com.kiero.presentation.kid.journey.component.KidJourneyHeader
import com.kiero.presentation.kid.journey.component.KidJourneyScheduleItem
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.state.KidJourneyState
import com.kiero.presentation.kid.journey.util.KidJourneyContentUtil
import com.kiero.presentation.kid.journey.viewmodel.KidJourneyViewModel

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
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchCoin()
        viewModel.fetchTodaySchedule()
    }

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is KidJourneySideEffect.ShowToast -> globalTrigger.showToast(sideEffect.message)
            is KidJourneySideEffect.ShowSnackbar -> globalTrigger.showSnackbar(
                SnackbarState(message = sideEffect.message)
            )
            KidJourneySideEffect.ShowDialog -> globalTrigger.dialogTrigger.show {}
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navigateToCamera()
        }
    }

    KidJourneyScreen(
        paddingValues = paddingValues,
        state = state,
        onButtonClick = {
            when (state.buttonType) {
                KidJourneyButtonType.AUTH -> {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        navigateToCamera()
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
                KidJourneyButtonType.FIRE -> {
                    navigateToFire()
                }
                else -> { }
            }
        },
        onNextClick = viewModel::onNextClick,
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidJourneyScreen(
    paddingValues: PaddingValues,
    state: KidJourneyState,
    onButtonClick: () -> Unit,
    onNextClick: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageWidth = screenWidth + 8.dp

    var showDialog by remember { mutableStateOf(false) }

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
            modifier = Modifier
                .wrapContentSize(
                    align = Alignment.TopCenter,
                    unbounded = true
                )
                .padding(top = 132.dp, bottom = 250.dp)
                .requiredWidth(imageWidth),
            contentScale = ContentScale.FillWidth
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

            KidSpeechField(
                name = "꾸비",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                isVisibleButton = state.shouldShowNextButton,
                onClick = { showDialog = true }
            ) {
                KidJourneyGoblinMessage(
                    content = state.content
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

        // 꾸비 gif
        KieroGifImage(
            drawableId = R.drawable.gif_kid_intro,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 250.dp, start = 8.dp, end = 8.dp, bottom = 150.dp)
        )
    }

    if (showDialog) {
        KieroDialog(
            onDismiss = { showDialog = false },
            title = "다음 여정으로 갈거야?",
            subDescription = "한 번 다음 여정으로 넘어가면 \n다시 지금 여정으로 돌아올 수 없어!",
            cancelAction = KieroCancelAction(
                text = "취소",
                onClick = { showDialog = false }
            ),
            confirmAction = KieroConfirmAction(
                text = "확인",
                onClick = {
                    showDialog = false
                    onNextClick()
                }
            ),
            content = {}
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
                header = KidJourneyHeaderUiModel(
                    kidName = "주완",
                    currentDate = "12월 5일 목요일",
                    coinCount = 350,
                    earnedStones = 5,
                    totalScheduleCount = 7
                ),
                content = KidJourneyContentUiModel.NowSchedule(
                    scheduleDetailId = 1,
                    scheduleName = "피아노 학원 가기",
                    stoneType = "용기의 불조각",
                    scheduleInfo = KidJourneyScheduleUiModel(
                        order = 4,
                        startTime = "14:00:00",
                        endTime = "16:00:00"
                    ),
                    isSkippable = true
                )
            ),
            onButtonClick = {},
            onNextClick = {}
        )
    }
}