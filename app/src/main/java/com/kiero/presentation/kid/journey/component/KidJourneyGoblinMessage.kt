package com.kiero.presentation.kid.journey.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel

@Composable
fun KidJourneyGoblinMessage(
    content: KidJourneyContentUiModel
) {
    val mainColor = KieroTheme.colors.main
    val defaultColor = KieroTheme.colors.gray300
    val textStyle = KieroTheme.typography.regular.body3

    when (content) {
        // "오늘은 휴식의 날인가봐!\n푹 쉬면서 내일의 여정을 위한 힘을 모으자!"
        is KidJourneyContentUiModel.NoSchedule -> {
            Text(
                text = "오늘은 휴식의 날인가봐!",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "푹 쉬면서 내일의 여정을 위한 힘을 모으자!",
                color = defaultColor,
                style = textStyle
            )
        }

        // "오늘도 내 불씨를 키워주러 왔구나!\n우리의 첫 번째 여정은 %s 야!"
        is KidJourneyContentUiModel.FirstSchedule -> {
            Text(
                text = "오늘도 내 불씨를 키워주러 왔구나!",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = buildAnnotatedString {
                    append("우리의 첫 번째 여정은 ")
                    withStyle(SpanStyle(color = mainColor)) {
                        append(content.scheduleName)
                    }
                    append(" 야!")
                },
                color = defaultColor,
                style = textStyle
            )
        }

        // "지금은 %s 의 시간이야!\n여정을 진행하면 %s 을 줄게."
        is KidJourneyContentUiModel.NowSchedule -> {
            Text(
                text = buildAnnotatedString {
                    append("지금은 ")
                    withStyle(SpanStyle(color = mainColor)) {
                        append(content.scheduleName)
                    }
                    append(" 의 시간이야!")
                },
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = buildAnnotatedString {
                    append("여정을 진행하면 ")
                    withStyle(SpanStyle(color = mainColor)) {
                        append(content.stoneType)
                    }
                    append(" 을 줄게.")
                },
                color = defaultColor,
                style = textStyle
            )
        }

        // "다음은 %s 의 시간이야!\n다음 여정을 진행하면 %s 을 줄게."
        is KidJourneyContentUiModel.NextSchedule -> {
            Text(
                text = buildAnnotatedString {
                    append("다음은 ")
                    withStyle(SpanStyle(color = mainColor)) {
                        append(content.scheduleName)
                    }
                    append(" 의 시간이야!")
                },
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = buildAnnotatedString {
                    append("다음 여정을 진행하면 ")
                    withStyle(SpanStyle(color = mainColor)) {
                        append(content.stoneType)
                    }
                    append(" 을 줄게.")
                },
                color = defaultColor,
                style = textStyle
            )
        }

        // "고마워 %s!\n오늘의 조각들이 모두 모였어! %s 을 피워줘!"
        is KidJourneyContentUiModel.FireNotLit -> {
            Text(
                text = "고마워 ${content.kidName}!",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = buildAnnotatedString {
                    append("오늘의 조각들이 모두 모였어! ")
                    withStyle(SpanStyle(color = mainColor)) {
                        append("영웅의 불꽃")
                    }
                    append(" 을 피워줘!")
                },
                color = defaultColor,
                style = textStyle
            )
        }

        // "오늘의 불조각을 모두 모았어\n내일도 우리 함께하자!"
        is KidJourneyContentUiModel.FireLit -> {
            Text(
                text = "오늘의 불조각을 모두 모았어",
                color = defaultColor,
                style = textStyle
            )
            Text(
                text = "내일도 우리 함께하자!",
                color = defaultColor,
                style = textStyle
            )
        }
    }
}

@Preview
@Composable
private fun KidJourneyGoblinMessagePreview() {
    KieroTheme {
        KidSpeechField(
            name = "꾸비"
        ) {
            KidJourneyGoblinMessage(
                content = KidJourneyContentUiModel.NowSchedule(
                    scheduleDetailId = 1,
                    scheduleName = "피아노 학원 가기",
                    stoneType = "용기의 불조각",
                    scheduleInfo = KidJourneyScheduleUiModel(
                        order = 4,
                        startTime = "14:00:00",
                        endTime = "16:00:00"
                    ),
                    isSkippable = true
                )
            )
        }
    }
}