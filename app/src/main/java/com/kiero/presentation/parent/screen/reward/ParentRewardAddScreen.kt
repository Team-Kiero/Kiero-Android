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
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.navigation.ParentReward
import com.kiero.presentation.parent.screen.reward.component.RewardNameTextField
import com.kiero.presentation.parent.screen.reward.component.RewardPriceInfo
import com.kiero.presentation.parent.screen.reward.component.RewardPriceSelect
import com.kiero.presentation.parent.screen.reward.model.RewardPriceDefaults
import com.kiero.presentation.parent.screen.reward.state.ParentRewardSideEffect
import com.kiero.presentation.parent.screen.reward.viewmodel.ParentAddRewardViewModel

@Composable
fun ParentRewardAddRoute(
    navigateUp: () -> Unit,
    viewModel: ParentAddRewardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ParentRewardSideEffect.ShowSnackBar -> {
                focusManager.clearFocus()
                globalTrigger.showToast(sideEffect.message)
            }
            ParentRewardSideEffect.NavigateUp -> {
                globalTrigger.onTabReselected(ParentReward)
                navigateUp()
            }
        }
    }

    ParentRewardAddScreen(
        isLoading = uiState.isLoading,
        nameState = viewModel.nameState,
        priceState = viewModel.priceState,
        focusRequester = focusRequester,
        onSaveClick = viewModel::createReward,
        onCancelClick = navigateUp,
        onValidatePrice = viewModel::validateAndFixPrice
    )
}

@Composable
private fun ParentRewardAddScreen(
    isLoading: Boolean,
    nameState: TextFieldState,
    priceState: TextFieldState,
    focusRequester: FocusRequester,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onValidatePrice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .noRippleClickable {
                onValidatePrice()
                focusManager.clearFocus()
            },
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
                if (!isLoading) onSaveClick()
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
                val current = priceState.text.toString().toIntOrNull() ?: RewardPriceDefaults.DEFAULT_PRICE
                val updated = (current + delta).coerceIn(
                    RewardPriceDefaults.MIN_PRICE,
                    RewardPriceDefaults.MAX_PRICE
                )
                priceState.setTextAndPlaceCursorAtEnd(updated.toString())
            },
            onValueAdjust = {
                onValidatePrice()
            }
        )
    }
}

@Preview
@Composable
private fun ParentRewardAddScreenPreview() {
    KieroTheme {
        ParentRewardAddScreen(
            isLoading = false,
            nameState = rememberTextFieldState(),
            priceState = rememberTextFieldState("20"),
            focusRequester = remember { FocusRequester() },
            onSaveClick = {},
            onCancelClick = {},
            onValidatePrice = {}
        )
    }
}
