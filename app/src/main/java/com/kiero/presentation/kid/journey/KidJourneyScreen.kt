package com.kiero.presentation.kid.journey

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.animation.KieroAnimationType
import com.kiero.core.designsystem.component.animation.KieroAnimationView
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.permission.PermissionChecker
import com.kiero.core.permission.model.PermissionType
import com.kiero.core.permission.ui.rememberPermissionRequester
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.journey.component.KidJourneyActionButton
import com.kiero.presentation.kid.journey.component.KidJourneyGoblinMessage
import com.kiero.presentation.kid.journey.component.KidJourneyHeader
import com.kiero.presentation.kid.journey.component.KidJourneyScheduleItem
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.state.KidJourneyState
import com.kiero.presentation.kid.journey.viewmodel.KidJourneyViewModel
import com.kiero.presentation.main.navigation.KidMainTab
import timber.log.Timber

@Composable
fun KidJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCamera: (Long, KidJourneyStoneType) -> Unit,
    navigateToFire: (String, Int) -> Unit,
    navigateToMap: (String) -> Unit,
    viewModel: KidJourneyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val refreshState = LocalRefreshState.current

    var showInitialPermissionDialog by remember { mutableStateOf(false) }
    val notificationDeniedCount by viewModel.notificationDeniedCount.collectAsStateWithLifecycle()

    val requestPushPermission = rememberPermissionRequester(
        type = PermissionType.POST_NOTIFICATIONS,
        deniedCount = notificationDeniedCount,
        onGranted = { viewModel.updatePushSetting(true) },
        onDenied = { viewModel.updatePushSetting(false) },
        onPermanentlyDenied = { viewModel.updatePushSetting(false) },
        onCountIncrease = { viewModel.increaseDeniedCount(PermissionType.POST_NOTIFICATIONS) }
    )

    LaunchedEffect(notificationDeniedCount) {
        val hasOsPermission = PermissionChecker.isGranted(context, PermissionType.POST_NOTIFICATIONS)
        if (viewModel.checkShouldShowPushPrompt(hasOsPermission, notificationDeniedCount)) {
            showInitialPermissionDialog = true
        }
    }

    LaunchedEffect(Unit) {
        refreshState.refreshEvent.collect { tab ->
            if (tab == KidMainTab.JOURNEY) {
                viewModel.fetchData()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchData()
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

    when (val state = state) {
        is UiState.Success -> {
            val onNavigateToCamera = {
                val content = state.data.content

                if (content is KidJourneyContentUiModel.ScheduledContent) {
                    navigateToCamera(content.scheduleDetailId!!, content.stoneType!!)
                }
            }

            val requestCamera = rememberPermissionRequester(
                type = PermissionType.CAMERA,
                deniedCount = state.data.permissionCameraDeniedCount,
                onGranted = onNavigateToCamera,
                onDenied = {
                    // Todo : 퍼미션 거부됨 스낵바 등
                },
                onPermanentlyDenied = {
                    // Todo : 퍼미션 거부 안내 UI 띄우기 - 2번 취소되었을 때 설정으로 이동
                    Timber.e("퍼미션 완전 거부")
                    //context.navigateToSettings(type = PermissionType.CAMERA)
                },
                onCountIncrease = viewModel::increaseDeniedCount,
            )

            KidJourneyScreen(
                paddingValues = paddingValues,
                state = state.data,
                onButtonClick = {
                    when (state.data.buttonType) {
                        KidJourneyButtonType.AUTH -> {
                            requestCamera()
                        }

                        KidJourneyButtonType.FIRE -> {
                            navigateToFire(
                                state.data.header!!.currentDate,
                                state.data.header.earnedStones!!
                            )
                        }

                        else -> {}
                    }
                },
                onNextClick = viewModel::onNextClick,
                onMapClick = { navigateToMap(state.data.header!!.currentDate) },
                navigateUp = navigateUp,
            )
        }

        UiState.Empty -> {}
        is UiState.Failure -> {}

        UiState.Loading -> KieroLoadingIndicator()
    }

    if (showInitialPermissionDialog) {
        KieroDialog(
            isDisabled = false,
            onDismiss = {
                showInitialPermissionDialog = false
            },
            title = "오늘의 여정을 놓치지 않게 해줄게!",
            subDescription = "여정의 중요한 알림을 받아볼 수 있어.",
            confirmAction = KieroConfirmAction(
                text = "알림받기",
                onClick = {
                    showInitialPermissionDialog = false
                    requestPushPermission()
                }
            )
        )
    }
}
@Composable
private fun KidJourneyScreen(
    paddingValues: PaddingValues,
    state: KidJourneyState,
    onButtonClick: () -> Unit,
    onNextClick: () -> Unit,
    onMapClick: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        val imageWidth = maxWidth + 8.dp

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
                KidJourneyHeader(
                    header = header,
                    isFireLit = state.buttonType == KidJourneyButtonType.FIRE
                )
            }

            AnimatedVisibility(
                visible = state.shouldShowSchedule && state.currentScheduleInfo != null,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
                label = "ScheduleItemAnimation"
            ) {
                Column {
                    Spacer(modifier = Modifier.height(21.dp))

                    state.currentScheduleInfo?.let { schedule ->
                        KidJourneyScheduleItem(item = schedule)
                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.End)
                    .dropShadow(
                        shape = CircleShape,
                        shadow = Shadow(
                            radius = 10.dp,
                            color = KieroTheme.colors.gray800,
                        )
                    )
                    .background(
                        color = KieroTheme.colors.gray800,
                        shape = CircleShape
                    )
                    .noRippleClickable {
                        onMapClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_kid_tab_journey),
                    contentDescription = null,
                    tint = KieroTheme.colors.white,
                    modifier = Modifier.size(16.dp)
                )
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
        KieroAnimationView(
            type = KieroAnimationType.Image(R.drawable.webp_kid_intro),
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
            state = KidJourneyState.FAKE,
            onButtonClick = {},
            onNextClick = {},
            onMapClick = {}
        )
    }
}
