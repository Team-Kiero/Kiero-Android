package com.kiero.presentation.parent.screen.reward

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.navigation.Reward
import com.kiero.presentation.parent.screen.reward.component.RewardNameTextField
import com.kiero.presentation.parent.screen.reward.component.RewardPriceInfo
import com.kiero.presentation.parent.screen.reward.component.RewardPriceSelect
import com.kiero.presentation.parent.screen.reward.model.RewardPriceDefaults
import com.kiero.presentation.parent.screen.reward.state.ParentRewardEditSideEffect
import com.kiero.presentation.parent.screen.reward.state.ParentRewardEditState
import com.kiero.presentation.parent.screen.reward.viewmodel.ParentRewardAddEditViewModel
import com.kiero.presentation.parent.screen.reward.viewmodel.ParentRewardViewModel

@Composable
fun ParentRewardEditRoute(
    navigateUp: () -> Unit,
    viewModel: ParentRewardViewModel = hiltViewModel(),
    addEditViewModel: ParentRewardAddEditViewModel = hiltViewModel(),
) {
    val rewardState by viewModel.state.collectAsStateWithLifecycle()
    val state by addEditViewModel.editState.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current

    val nameState = rememberTextFieldState()
    val priceState = rememberTextFieldState()

    LaunchedEffect(rewardState) {
        val selected = (rewardState as? UiState.Success)?.data?.selectedReward ?: return@LaunchedEffect
        addEditViewModel.initReward(selected)
        nameState.setTextAndPlaceCursorAtEnd(selected.name)
        priceState.setTextAndPlaceCursorAtEnd(selected.price.toString())
    }

    addEditViewModel.editSideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ParentRewardEditSideEffect.ShowSnackBar -> {
                focusManager.clearFocus()
                globalTrigger.showSnackbar(SnackbarState(message = sideEffect.message))
            }
            ParentRewardEditSideEffect.NavigateUp -> {
                globalTrigger.onTabReselected(Reward)
                navigateUp()
            }
        }
    }

    ParentRewardEditScreen(
        state = state,
        nameState = nameState,
        priceState = priceState,
        onSaveClick = {
            addEditViewModel.updateReward(
                nameState.text.toString(),
                priceState.text.toString().toIntOrNull() ?: 0
            )
        },
        onCancelClick = navigateUp,
    )
}
@Composable
private fun ParentRewardEditScreen(
    state: ParentRewardEditState,
    nameState: TextFieldState,
    priceState: TextFieldState,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val globalTrigger = LocalGlobalUiEventTrigger.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .noRippleClickable { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        KieroTopbar(
            title = "보상수정",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = R.drawable.ic_check,
            leftIconClick = onCancelClick,
            rightIconClick = {
                if (!state.isLoading) onSaveClick()
            },
        )

        RewardNameTextField(state = nameState)

        RewardPriceInfo()

        RewardPriceSelect(
            textFieldState = priceState,
            onPriceClick = { delta ->
                val current = priceState.text.toString().toIntOrNull() ?: 0
                val updated = RewardPriceDefaults.applyChange(current, delta)
                priceState.setTextAndPlaceCursorAtEnd(updated.toString())
            },
            onValueAdjust = { adjustedValue ->
                priceState.setTextAndPlaceCursorAtEnd(adjustedValue.toString())
                if (adjustedValue == RewardPriceDefaults.MAX_PRICE) {
                    globalTrigger.showSnackbar(SnackbarState(message = "최대 보상은 500개입니다"))
                }
            }
        )
    }
}
@Preview
@Composable
private fun ParentRewardEditScreenPreview() {
    KieroTheme {
        ParentRewardEditScreen(
            state = ParentRewardEditState(),
            nameState = rememberTextFieldState("용돈 5000원 받기"),
            priceState = rememberTextFieldState("500"),
            onSaveClick = {},
            onCancelClick = {},
        )
    }
}