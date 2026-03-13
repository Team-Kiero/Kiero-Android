package com.kiero.presentation.parent.screen.reward

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.navigation.Reward
import com.kiero.presentation.parent.screen.reward.component.RewardNameTextField
import com.kiero.presentation.parent.screen.reward.component.RewardPriceInfo
import com.kiero.presentation.parent.screen.reward.component.RewardPriceSelect
import com.kiero.presentation.parent.screen.reward.model.RewardPriceDefaults
import com.kiero.presentation.parent.screen.reward.state.ParentRewardAddSideEffect
import com.kiero.presentation.parent.screen.reward.state.ParentRewardAddState
import com.kiero.presentation.parent.screen.reward.viewmodel.ParentRewardAddEditViewModel

@Composable
fun ParentRewardAddRoute(
    navigateUp: () -> Unit,
    viewModel: ParentRewardAddEditViewModel = hiltViewModel(),
) {
    val state by viewModel.addState.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    viewModel.addSideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ParentRewardAddSideEffect.ShowSnackBar -> {
                focusManager.clearFocus()
                globalTrigger.showSnackbar(SnackbarState(message = sideEffect.message))
            }
            ParentRewardAddSideEffect.NavigateUp -> {
                globalTrigger.onTabReselected(Reward)
                navigateUp()
            }
        }
    }

    ParentRewardAddScreen(
        state = state,
        onSaveClick = viewModel::createReward,
        onCancelClick = navigateUp,
        focusRequester = focusRequester
    )
}

@Composable
private fun ParentRewardAddScreen(
    state: ParentRewardAddState,
    focusRequester: FocusRequester,
    onSaveClick: (name: String, price: Int) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current
    val nameState = rememberTextFieldState()
    val priceState = rememberTextFieldState(RewardPriceDefaults.DEFAULT_PRICE.toString())

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
            title = "보상 추가",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = R.drawable.ic_check,
            leftIconClick = onCancelClick,
            rightIconClick = {
                if (!state.isLoading) {
                    onSaveClick(
                        nameState.text.toString(),
                        priceState.text.toString().toIntOrNull() ?: 0,
                    )
                }
            },
        )

        RewardNameTextField(
            state = nameState,
            modifier = Modifier.focusRequester(focusRequester)
        )

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
private fun ParentRewardAddScreenPreview() {
    KieroTheme {
        ParentRewardAddScreen(
            state = ParentRewardAddState(),
            focusRequester = remember { FocusRequester() },
            onSaveClick = { _, _ -> },
            onCancelClick = {},
        )
    }
}