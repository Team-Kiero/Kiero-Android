package com.kiero.presentation.kid.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.SpeechField

@Composable
fun KidOnboardingRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToKid: () -> Unit,
) {
    KidOnboardingScreen(
        paddingValues = paddingValues,
        navigateToKid = navigateToKid,
        onSkipClick = {},
        onNextClick = {}
    )
}

@Composable
fun KidOnboardingScreen(
    paddingValues: PaddingValues,
    navigateToKid: () -> Unit,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentStep by remember { mutableStateOf(OnboardingUiModel.STORY1) }
    val currentDescription = DescriptionModel.getDescription(
        step = currentStep.step,
        mainColor = KieroTheme.colors.main
    )
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
            .padding(paddingValues),
    ) {
        Image(
            painter = painterResource(id = currentStep.backImage),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(512.dp))

            SpeechField(
                name = "꾸비",
                modifier = Modifier
                    .noRippleClickable(onClick = moveToNextStep)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = currentDescription.description,
                    color = KieroTheme.colors.gray300,
                    style = KieroTheme.typography.regular.body3,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (currentStep == OnboardingUiModel.STORY5) {
                KieroButtonMedium(
                    text = "여정 시작하기",
                    onClick = navigateToKid,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 41.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun KidOnboardingStoryScreenPreview() {
    KieroTheme {
        KidOnboardingScreen(
            paddingValues = PaddingValues(),
            navigateToKid = {},
            onSkipClick = {},
            onNextClick = {}
        )
    }
}