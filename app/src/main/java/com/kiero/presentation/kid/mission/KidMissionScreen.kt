package com.kiero.presentation.kid.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.component.pulltorefresh.KieroPullToRefresh
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.mission.component.KidMissionItem
import com.kiero.presentation.kid.mission.state.KidMissionState

@Composable
fun KidMissionRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidMissionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val state = state) {
        is UiState.Success -> {
            KidMissionScreen(
                paddingValues = paddingValues,
                state = state.data,
                navigateUp = navigateUp,
                onMissionCompleted = viewModel::openMissionDialog,
                onRefresh = { viewModel.fetchMissions(isRefreshing = true) },
                dismissDialog = viewModel::dismissDialog,
                onClickConfirm = viewModel::onMissionCompleted
            )
        }

        UiState.Empty -> {}
        is UiState.Failure -> {}

        UiState.Loading -> {
            KieroLoadingIndicator()
        }
    }
}

@Composable
private fun KidMissionScreen(
    paddingValues: PaddingValues,
    state: KidMissionState,
    navigateUp: () -> Unit,
    onRefresh: () -> Unit,
    onMissionCompleted: (missionId: Long) -> Unit,
    dismissDialog: () -> Unit,
    onClickConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val painterGoblin = painterResource(id = R.drawable.img_kid_wish_goblin)

    KieroPullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(KieroTheme.colors.black)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KidProfileChip(
                        kidName = state.kidName
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    KieroChip(
                        action = KieroCoinAction(
                            coinCount = state.coinUiModel.coinAmount,
                            isEnabled = true,
                            onClick = {}
                        )
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterGoblin,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .forcePixelToDp(painterGoblin)
                            .offset(y = (-30).dp)
                    )

                    KidSpeechField(
                        name = "꾸비",
                        isVisibleButton = false,
                        modifier = Modifier.padding(top = 40.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("우리 함께 ")
                                withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                                    append("멋진 금화")
                                }
                                append(" 를 만들어볼까?")
                            },
                            color = KieroTheme.colors.gray300
                        )
                    }
                }

                Text(
                    text = "미션",
                    style = KieroTheme.typography.semiBold.title4,
                    color = KieroTheme.colors.gray200,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(alpha = if (state.kidMissionByDateList.missionsByDate.isEmpty()) 0f else 1f)
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Start
                )
            }

            if (state.kidMissionByDateList.missionsByDate.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.6f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "아직 등록된 미션이 없어!",
                            style = KieroTheme.typography.semiBold.title3,
                            color = KieroTheme.colors.gray400,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                state.kidMissionByDateList.missionsByDate.forEach { section ->
                    item {
                        Text(
                            text = section.title,
                            style = KieroTheme.typography.regular.body4,
                            color = KieroTheme.colors.gray200,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    items(
                        items = section.missions,
                        key = { it.id },
                    ) { item ->
                        KidMissionItem(
                            missionTitle = item.name,
                            missionReward = item.reward,
                            isCompleted = item.isCompleted,
                            onClickButton = {
                                onMissionCompleted(item.id)
                            }
                        )
                    }
                }
            }
        }

        if (state.isVisibleDialog) {
            KieroDialog (
                onDismiss = dismissDialog,
                isDisabled = state.isCompletedMission,
                title = if (!state.isCompletedMission) "[${state.selectedMissionItem!!.name}]" else null,
                subDescription = if (!state.isCompletedMission) "미션을 완료했다면\n" +
                        "아래 버튼을 눌러줘!" else "금 나와라 뚝딱!\n" +
                        "금화 ${state.selectedMissionItem?.reward}를 만들었어!",
                cancelAction = if (state.isCompletedMission) {
                    null
                } else {
                    KieroCancelAction(
                        onClick = dismissDialog
                    )
                },
                confirmAction = KieroConfirmAction(
                    text = "확인",
                    onClick = {
                        if (state.isCompletedMission) {
                            dismissDialog()
                        } else {
                            onClickConfirm()
                        }
                    }
                )
            ) {
                if (state.isCompletedMission) {
                    val coinImage = painterResource(R.drawable.img_kid_camera_goblin)

                    Image(
                        painter = coinImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(
                            width = 62.dp,
                            height = 70.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun KidWishScreenPreview() {
    KieroTheme {
        KidMissionScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            onMissionCompleted = {},
            state = KidMissionState(),
            onRefresh = {},
            dismissDialog = {},
            onClickConfirm = {}
        )
    }
}