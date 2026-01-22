package com.kiero.presentation.kid.onboarding.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.onboarding.model.OnboardingUiModel


@Composable
fun KidOnboardingMessage(
    step: OnboardingUiModel,
    kidName: String = ""
) {
    val mainColor = KieroTheme.colors.main
    val defaultColor = KieroTheme.colors.gray300
    val textStyle = KieroTheme.typography.regular.body3

    when (step) {
        OnboardingUiModel.STORY1 -> {
            Text(
                text = "드디어 만났다! 나의 짝궁 $kidName",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "난 꼬마 히어로 꾸비야. 우리 같이 모험을 떠나볼까?",
                color = defaultColor,
                style = textStyle
            )
        }
        OnboardingUiModel.STORY2 -> {
            Text(
                text = "다른 도깨비들은 장난치는 걸 좋아하지만,",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "난 '영웅의 불씨'를 품고 태어난 특별한 도꺠비야!",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = buildAnnotatedString {
                    append("너의 노력을 멋진 소원으로 바꾸는 ")
                    withStyle(style = SpanStyle(color = mainColor)) {
                        append("꼬마 히어로")
                    }
                    append(" 지.")
                },
                color = defaultColor,
                style = textStyle
            )
        }
        OnboardingUiModel.STORY3 -> {
            Text(
                text = "그런데 큰일이야...",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "배에 있는 '영웅의 불씨'가 자꾸 꺼지려고 해.",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "나 혼자서는 지킬 수 없거든",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = mainColor)) {
                        append("오직 너만이 이 불씨를 다시 키울수 있어!")
                    }
                },
                color = defaultColor,
                style = textStyle
            )
        }
        OnboardingUiModel.STORY4 -> {
            Text(
                text = "오늘의 여정을 따라 하루를 보내고",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "불조각을 나에게 건네줘!",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = buildAnnotatedString {
                    append("너가 준 ")
                    withStyle(style = SpanStyle(color = mainColor)) {
                        append("[용기,인내,지혜의 불조각]")
                    }
                    append(" 이")
                },
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "내 마음이 불꽃을 키워줄거야",
                color = defaultColor,
                style = textStyle
            )
        }
        OnboardingUiModel.STORY5 -> {
            Text(
                text = "그 힘으로 내가 반짝이는 금화를 만들어줄게!",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "소원의 우물에서 금화를 통해",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "너의 소원을 이룰 수 있을거야!",
                color = defaultColor,
                style = textStyle
            )
        }
    }
}