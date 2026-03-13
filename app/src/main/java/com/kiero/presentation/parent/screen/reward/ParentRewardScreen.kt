package com.kiero.presentation.parent.screen.reward

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
    navigateToRewardEdit: () -> Unit,
    viewModel: ParentRewardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val gridState = rememberLazyGridState()

    LaunchedEffect(globalTrigger) {
        globalTrigger.tabReselectedEvent.collect { route ->
            if (route == Reward) {
                gridState.animateScrollToItem(0)
                viewModel.hideBottomSheet()
                viewModel.hideDeleteDialog()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchRewards()
    }

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ParentRewardSideEffect.ShowSnackBar -> globalTrigger.showSnackbar(
                SnackbarState(message = sideEffect.message)
            )
        }
    }

    when (val uiState = state) {
        is UiState.Success -> {
            ParentRewardScreen(
                paddingValues = paddingValues,
                state = uiState.data,
                navigateToRewardAdd = navigateToRewardAdd,
                gridState = gridState,
                onRewardClick = viewModel::showBottomSheet,
                onBottomSheetDismiss = viewModel::hideBottomSheet,
                onEditClick = {
                    viewModel.hideBottomSheet()
                    navigateToRewardEdit()
                },
                onDeleteClick = viewModel::showDeleteDialog,
                onDeleteDialogDismiss = viewModel::hideDeleteDialog,
                onDeleteConfirm = viewModel::deleteReward,
            )
        }
        UiState.Loading -> KieroLoadingIndicator()
        is UiState.Failure -> {}
        UiState.Empty -> {}
    }
}

@Composable
private fun ParentRewardScreen(
    paddingValues: PaddingValues,
    state: ParentRewardState,
    gridState: LazyGridState,
    navigateToRewardAdd: () -> Unit,
    onRewardClick: (ParentRewardUiModel) -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDeleteDialogDismiss: () -> Unit,
    onDeleteConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ParentTopbar(
                title = "보상",
                onAlarmClick = {},
            )

            if (state.rewards.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(175.dp))

                    Text(
                        text = "등록된 보상 쿠폰이 없어요!\n우측 하단 버튼을 눌러 보상을 추가해보세요!",
                        style = KieroTheme.typography.semiBold.title3,
                        color = KieroTheme.colors.gray400,
                        textAlign = TextAlign.Center,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_parent_no_alarm),
                        contentDescription = null,
                        modifier = Modifier,
                        contentScale = ContentScale.FillWidth,
                    )
                }
            } else {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(13.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp),
                ) {
                    items(
                        items = state.rewards,
                        key = { it.couponId },
                    ) { reward ->
                        ParentRewardCard(
                            name = reward.name,
                            price = reward.price,
                            onClick = { onRewardClick(reward) },
                        )
                    }
                }
            }
        }

        ParentFloatingButton(
            buttonColor = KieroTheme.colors.white,
            onActiveClick = navigateToRewardAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 31.dp, bottom = 19.dp + paddingValues.calculateBottomPadding())
        )
    }

    if (state.isBottomSheetVisible && state.selectedReward != null) {
        ParentRewardBottomSheet(
            reward = state.selectedReward,
            onDismissRequest = onBottomSheetDismiss,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
        )
    }

    if (state.isDeleteDialogVisible && state.selectedReward != null) {
        ParentRewardDeleteDialog(
            reward = state.selectedReward,
            onDismiss = onDeleteDialogDismiss,
            onConfirm = onDeleteConfirm,
        )
    }
}


@Preview
@Composable
private fun ParentRewardScreenPreview() {
    KieroTheme {
        ParentRewardScreen(
            paddingValues = PaddingValues(),
            gridState = rememberLazyGridState(),
            state = ParentRewardState(),
            navigateToRewardAdd = {},
            onRewardClick = {},
            onBottomSheetDismiss = {},
            onEditClick = {},
            onDeleteClick = {},
            onDeleteDialogDismiss = {},
            onDeleteConfirm = {},
        )
    }
}
@Preview
@Composable
private fun ParentRewardScreenWithItemsPreview() {
    KieroTheme {
        ParentRewardScreen(
            paddingValues = PaddingValues(),
            gridState = rememberLazyGridState(),
            state = ParentRewardState(
                rewards = persistentListOf(
                    ParentRewardUiModel(couponId = 1L, name = "용돈 5000원 받기", price = 350),
                    ParentRewardUiModel(couponId = 2L, name = "게임시간 30분 추가", price = 50),
                    ParentRewardUiModel(couponId = 3L, name = "저녁으로 치킨먹기", price = 100),
                    ParentRewardUiModel(couponId = 4L, name = "친구랑 파자마파티", price = 200),
                    ParentRewardUiModel(couponId = 5L, name = "주말에 놀이공원 가기", price = 500),
                    ParentRewardUiModel(couponId = 6L, name = "갖고 싶던 장난감 사기", price = 450),
                    ParentRewardUiModel(couponId = 7L, name = "유튜브 1시간 시청권", price = 30),
                    ParentRewardUiModel(couponId = 8L, name = "가족 다같이 영화보기", price = 150),
                    ParentRewardUiModel(couponId = 9L, name = "원하는 아이스크림 사먹기", price = 20),
                    ParentRewardUiModel(couponId = 10L, name = "심부름 1회 면제권", price = 80),
                )
            ),
            navigateToRewardAdd = {},
            onRewardClick = {},
            onBottomSheetDismiss = {},
            onEditClick = {},
            onDeleteClick = {},
            onDeleteDialogDismiss = {},
            onDeleteConfirm = {},
        )
    }
}