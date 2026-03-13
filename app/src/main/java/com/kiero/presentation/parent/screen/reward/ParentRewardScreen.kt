package com.kiero.presentation.parent.screen.reward

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.component.ParentFloatingButton
import com.kiero.presentation.parent.component.ParentTopbar
import com.kiero.presentation.parent.navigation.Reward
import com.kiero.presentation.parent.screen.reward.component.ParentRewardBottomSheet
import com.kiero.presentation.parent.screen.reward.component.ParentRewardCard
import com.kiero.presentation.parent.screen.reward.component.ParentRewardDeleteDialog
import com.kiero.presentation.parent.screen.reward.model.ParentRewardUiModel
import com.kiero.presentation.parent.screen.reward.state.ParentRewardSideEffect
import com.kiero.presentation.parent.screen.reward.state.ParentRewardState
import com.kiero.presentation.parent.screen.reward.viewmodel.ParentRewardViewModel
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ParentRewardRoute(
    paddingValues: PaddingValues,
    navigateToRewardAdd: () -> Unit,
    navigateToRewardEdit: (Long) -> Unit,
    viewModel: ParentRewardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val gridState = rememberLazyGridState()

    var isSheetVisible by remember { mutableStateOf(false) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var shouldScrollToTop by remember { mutableStateOf(false) }

    // GNB 재클릭 처리
    LaunchedEffect(globalTrigger) {
        globalTrigger.tabReselectedEvent.collect { route ->
            if (route == Reward) {
                viewModel.fetchRewards()
                shouldScrollToTop = true
                isSheetVisible = false
                isDialogVisible = false
            }
        }
    }

    // 스크롤 리셋 처리
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success && shouldScrollToTop) {
            gridState.scrollToItem(0)
            shouldScrollToTop = false
        }
    }

    // 초기 로드
    LaunchedEffect(Unit) { viewModel.fetchRewards() }

    // 사이드 이펙트 처리
    viewModel.sideEffect.collectSideEffect { sideEffect ->
        if (sideEffect is ParentRewardSideEffect.ShowSnackBar) {
            globalTrigger.showSnackbar(SnackbarState(message = sideEffect.message))
        }
    }

    // 메인 레이아웃
    when (val state = uiState) {
        is UiState.Success -> {
            ParentRewardScreen(
                paddingValues = paddingValues,
                state = state.data,
                gridState = gridState,
                isSheetVisible = isSheetVisible,
                isDialogVisible = isDialogVisible,
                onAddClick = navigateToRewardAdd,
                onRewardClick = { reward ->
                    viewModel.selectReward(reward)
                    isSheetVisible = true
                },
                onEditClick = {
                    state.data.selectedReward?.couponId?.let {
                        isSheetVisible = false
                        navigateToRewardEdit(it)
                    }
                },
                onDeleteClick = {
                    isSheetVisible = false
                    isDialogVisible = true
                },
                onBottomSheetDismiss = { isSheetVisible = false },
                onDeleteDialogDismiss = {
                    isDialogVisible = false
                    isSheetVisible = true
                },
                onDeleteConfirm = {
                    viewModel.deleteReward()
                    isDialogVisible = false
                }
            )
        }
        UiState.Loading -> KieroLoadingIndicator()
        else -> Unit
    }
}

@Composable
private fun ParentRewardScreen(
    paddingValues: PaddingValues,
    state: ParentRewardState,
    gridState: LazyGridState,
    isSheetVisible: Boolean,
    isDialogVisible: Boolean,
    onAddClick: () -> Unit,
    onRewardClick: (ParentRewardUiModel) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onDeleteDialogDismiss: () -> Unit,
    onDeleteConfirm: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ParentTopbar(title = "보상", onAlarmClick = {})

            if (state.rewards.isEmpty()) {
                EmptyRewardContent()
            } else {
                RewardGridContent(
                    gridState = gridState,
                    rewards = state.rewards,
                    onRewardClick = onRewardClick
                )
            }
        }

        ParentFloatingButton(
            buttonColor = KieroTheme.colors.white,
            onActiveClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 31.dp, bottom = 19.dp + paddingValues.calculateBottomPadding())
        )

        RewardOverlays(
            state = state,
            isSheetVisible = isSheetVisible,
            isDialogVisible = isDialogVisible,
            onBottomSheetDismiss = onBottomSheetDismiss,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            onDeleteDialogDismiss = onDeleteDialogDismiss,
            onDeleteConfirm = onDeleteConfirm
        )
    }
}

@Composable
private fun EmptyRewardContent() {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "등록된 보상 쿠폰이 없어요!\n우측 하단 버튼을 눌러 보상을 추가해보세요!",
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.gray400,
            textAlign = TextAlign.Center,
        )
        Image(
            painter = painterResource(id = R.drawable.img_parent_no_alarm),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )
    }
}

@Composable
private fun RewardGridContent(
    gridState: LazyGridState,
    rewards: List<ParentRewardUiModel>,
    onRewardClick: (ParentRewardUiModel) -> Unit
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        items(items = rewards, key = { it.couponId }) { reward ->
            ParentRewardCard(
                name = reward.name,
                price = reward.price,
                onClick = { onRewardClick(reward) },
            )
        }
    }
}

@Composable
private fun RewardOverlays(
    state: ParentRewardState,
    isSheetVisible: Boolean,
    isDialogVisible: Boolean,
    onBottomSheetDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDeleteDialogDismiss: () -> Unit,
    onDeleteConfirm: () -> Unit
) {
    val selected = state.selectedReward ?: return

    if (isSheetVisible) {
        ParentRewardBottomSheet(
            reward = selected,
            onDismissRequest = onBottomSheetDismiss,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
        )
    }

    if (isDialogVisible) {
        ParentRewardDeleteDialog(
            reward = selected,
            onDismiss = onDeleteDialogDismiss,
            onConfirm = onDeleteConfirm,
        )
    }
}
@Preview
@Composable
private fun ParentRewardScreenEmptyPreview() {
    KieroTheme {
        ParentRewardScreen(
            paddingValues = PaddingValues(bottom = 56.dp),
            state = ParentRewardState(rewards = persistentListOf()),
            gridState = rememberLazyGridState(),
            isSheetVisible = false,
            isDialogVisible = false,
            onAddClick = {},
            onRewardClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            onDeleteDialogDismiss = {},
            onDeleteConfirm = {},
        )
    }
}

@Preview
@Composable
private fun ParentRewardScreenListPreview() {
    val sampleRewards = persistentListOf(
        ParentRewardUiModel(couponId = 1L, name = "용돈 5,000원 받기", price = 350),
        ParentRewardUiModel(couponId = 2L, name = "게임시간 30분 추가", price = 50),
        ParentRewardUiModel(couponId = 3L, name = "저녁으로 치킨먹기", price = 100),
        ParentRewardUiModel(couponId = 4L, name = "친구랑 파자마파티", price = 200),
    )

    KieroTheme {
        ParentRewardScreen(
            paddingValues = PaddingValues(bottom = 56.dp),
            state = ParentRewardState(rewards = sampleRewards),
            gridState = rememberLazyGridState(),
            isSheetVisible = false,
            isDialogVisible = false,
            onAddClick = {},
            onRewardClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            onDeleteDialogDismiss = {},
            onDeleteConfirm = {},
        )
    }
}

@Preview
@Composable
private fun ParentRewardScreenBottomSheetPreview() {
    val sampleReward = ParentRewardUiModel(couponId = 1L, name = "용돈 5,000원 받기", price = 350)

    KieroTheme {
        ParentRewardScreen(
            paddingValues = PaddingValues(bottom = 56.dp),
            state = ParentRewardState(
                rewards = persistentListOf(sampleReward),
                selectedReward = sampleReward
            ),
            gridState = rememberLazyGridState(),
            isSheetVisible = true,
            isDialogVisible = false,
            onAddClick = {},
            onRewardClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            onDeleteDialogDismiss = {},
            onDeleteConfirm = {},
        )
    }
}

@Preview
@Composable
private fun ParentRewardScreenDeleteDialogPreview() {
    val sampleReward = ParentRewardUiModel(couponId = 1L, name = "용돈 5,000원 받기", price = 350)

    KieroTheme {
        ParentRewardScreen(
            paddingValues = PaddingValues(bottom = 56.dp),
            state = ParentRewardState(
                rewards = persistentListOf(sampleReward),
                selectedReward = sampleReward
            ),
            gridState = rememberLazyGridState(),
            isSheetVisible = false,
            isDialogVisible = true,
            onAddClick = {},
            onRewardClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            onDeleteDialogDismiss = {},
            onDeleteConfirm = {},
        )
    }
}