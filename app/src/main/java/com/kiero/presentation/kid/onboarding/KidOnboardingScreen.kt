package com.kiero.presentation.kid.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.onboarding.component.KidOnboardingMessage
import com.kiero.presentation.kid.onboarding.model.OnboardingUiModel
import com.kiero.presentation.kid.onboarding.state.KidOnboardingSideEffect
import com.kiero.presentation.kid.onboarding.viewmodel.KidOnboardingViewModel

@Composable
fun KidOnboardingRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToKid: () -> Unit,
    viewModel: KidOnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            KidOnboardingSideEffect.NavigateToKid -> navigateToKid()
        }
    }

    when (val uiState = state) {
        is UiState.Loading -> {
            KieroLoadingIndicator()
        }
        is UiState.Success -> {
            KidOnboardingScreen(
                paddingValues = paddingValues,
                kidName = uiState.data.kidName,
                navigateToKid = viewModel::startJourney,
                onSkipClick = {},
                onNextClick = {}
            )
        }
        is UiState.Failure -> {}
        UiState.Empty -> {}
    }
}

@Composable
fun KidOnboardingScreen(
    paddingValues: PaddingValues,
    kidName: String,
    navigateToKid: () -> Unit,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentStep by remember { mutableStateOf(OnboardingUiModel.STORY1) }

    val moveToNextStep = {
        currentStep = when (currentStep) {
            OnboardingUiModel.STORY1 -> OnboardingUiModel.STORY2
            OnboardingUiModel.STORY2 -> OnboardingUiModel.STORY3
            OnboardingUiModel.STORY3 -> OnboardingUiModel.STORY4
            OnboardingUiModel.STORY4 -> OnboardingUiModel.STORY5
            OnboardingUiModel.STORY5 -> {
                navigateToKid()
                currentStep
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
    ) {
        Image(
            painter = painterResource(id = currentStep.backImage),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (currentStep == OnboardingUiModel.STORY5) {
                KidSpeechField(
                    name = "꾸비",
                    modifier = Modifier
                        .noRippleClickable(onClick = moveToNextStep)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 11.dp),
                ) {
                    KidOnboardingMessage(step = currentStep)
                }
            } else {
                KidSpeechField(
                    name = "꾸비",
                    modifier = Modifier
                        .noRippleClickable(onClick = moveToNextStep)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 11.dp),
                    buttonText = "다음",
                    isVisibleButton = true,
                    nextButtonColor = KieroTheme.colors.main,
                    onClick = moveToNextStep
                ) {
                    KidOnboardingMessage(step = currentStep, kidName = kidName)
                }
            }

            KieroButtonMedium(
                text = "여정 시작하기",
                onClick = navigateToKid,
                modifier = Modifier
                    .alpha(if (currentStep == OnboardingUiModel.STORY5) 1f else 0f)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 55.dp)
            )
        }
    }
}

@Preview
@Composable
private fun KidOnboardingStoryScreenPreview() {
    KieroTheme {
        KidOnboardingScreen(
            paddingValues = PaddingValues(),
            kidName = "",
            navigateToKid = {},
            onSkipClick = {},
            onNextClick = {}
        )
    }
}