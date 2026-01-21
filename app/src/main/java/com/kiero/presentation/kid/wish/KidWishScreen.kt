package com.kiero.presentation.kid.wish

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
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
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.wish.component.KidWishDescription
import com.kiero.presentation.kid.wish.component.KidWishGridList
import com.kiero.presentation.kid.wish.model.KidWishUiModel
import com.kiero.presentation.kid.wish.preview.KidWishPreviewProvider
import com.kiero.presentation.kid.wish.state.KidWishSideEffect
import com.kiero.presentation.kid.wish.state.KidWishState
import com.kiero.presentation.kid.wish.viewmodel.KidWishViewModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun KidWishRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidWishViewModel = hiltViewModel()
) {
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSideEffect {
        when(it) {
            is KidWishSideEffect.ShowSnackBar -> {
                globalTrigger.showSnackbar(
                    SnackbarState(
                        message = it.message
                    )
                )
            }
        }
    }

    when(val state = state) {
        UiState.Loading -> {
            KieroLoadingIndicator()
        }
        is UiState.Success -> {
            with(state) {
                KieroPullToRefresh(
                    isRefreshing = data.isRefreshing,
                    onRefresh = { viewModel.fetchWish(isRefresh = true) }
                ) {
                    KidWishScreen(
                        paddingValues = paddingValues,
                        state = data,
                        navigateUp = navigateUp,
                        onClickWish = viewModel::openDialogWithItem
                    )

                    if (data.isVisibleDialog) {
                        KieroDialog(
                            onDismiss = viewModel::dismissDialog,
                            title = if (!data.isCompletedWish) data.selectedWishItem!!.name else null,
                            subDescription = if (data.isCompletedWish) "${data.selectedWishItem!!.name}를 \n획득했어!" else "금화를 사용해 소원을 빌까?",
                            cancelAction = if (data.isCompletedWish) {
                                null
                            } else {
                                KieroCancelAction(
                                    onClick = {
                                        viewModel.dismissDialog()
                                    }
                                )
                            },
                            confirmAction = KieroConfirmAction(
                                text = "확인",
                                onClick = {
                                    if (data.isCompletedWish) {
                                        viewModel.prayWish(data.selectedWishItem?.couponId ?: -1)
                                    } else {
                                        viewModel.dismissDialog()
                                    }
                                }
                            )
                        ) {
                            if (!data.isCompletedWish) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val coinImage = painterResource(R.drawable.img_kid_coin)

                                    Image(
                                        painter = coinImage,
                                        contentDescription = null,
                                        modifier = Modifier.forcePixelToDp(coinImage)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = "${data.selectedWishItem?.price} 개",
                                        color = KieroTheme.colors.main,
                                        style = KieroTheme.typography.semiBold.title4,
                                    )
                                }
                            } else {
                                val coinImage = painterResource(R.drawable.img_kid_camera_goblin)

                                Image(
                                    painter = coinImage,
                                    contentDescription = null,
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
        }

        UiState.Empty -> {}
        is UiState.Failure -> {}
    }
}

@Composable
private fun KidWishScreen(
    paddingValues: PaddingValues,
    state: KidWishState,
    navigateUp: () -> Unit,
    onClickWish : (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                color = KieroTheme.colors.black
            )
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 25.dp),
    ) {
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

        Spacer(modifier = Modifier.height(15.dp))

        KidWishDescription(
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(17.dp))

        HorizontalDivider(
            thickness = 2.dp,
            color = KieroTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.height(17.dp))

        KidWishGridList(
            modifier = Modifier.fillMaxWidth(),
            wishList = state.kidWishList,
            onClickWish = onClickWish
        )
    }
}

@Composable
@Preview
private fun KidWishScreenPreview(
    @PreviewParameter(KidWishPreviewProvider::class) uiState: UiState<ImmutableList<KidWishUiModel>>
) {
    KieroTheme {
        val wishList = (uiState as? UiState.Success)?.data ?: KidWishState.FAKE

        val state = KidWishState(
            kidWishList = wishList
        )

        KidWishScreen(
            paddingValues = PaddingValues(),
            state = state,
            navigateUp = {},
            onClickWish = {}
        )
    }
}