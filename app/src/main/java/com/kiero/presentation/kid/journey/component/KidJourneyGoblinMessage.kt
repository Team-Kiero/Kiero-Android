package com.kiero.presentation.kid.journey.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.model.KidJourneyContentModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleModel
import java.time.LocalTime

@Composable
fun KidJourneyGoblinMessage(
    content: KidJourneyContentModel,
    messageRes: Int,
    messageArgs: Array<Any>
) {
    when (content) {
        is KidJourneyContentModel.NoSchedule,
        is KidJourneyContentModel.FireLit -> {
            Text(
                text = stringResource(messageRes),
                color = KieroTheme.colors.gray300,
                style = KieroTheme.typography.regular.body3
            )
        }
        is KidJourneyContentModel.FirstSchedule -> {
            // journey_first_schedule: 오늘도 내 불씨를 키워주러 왔구나!\n우리의 첫 번째 여정은 %s 야!
            StyledMessage(
                messageRes = messageRes,
                highlightIndices = listOf(0),
                args = messageArgs
            )
        }
        is KidJourneyContentModel.NowSchedule,
        is KidJourneyContentModel.NextSchedule -> {
            // journey_now_schedule: 지금은 %s 의 시간이야!\n여정을 진행하면 %s 을 줄게.
            // journey_next_schedule: 다음은 %s 의 시간이야!\n다음 여정을 진행하면 %s 을 줄게.
            StyledMessage(
                messageRes = messageRes,
                highlightIndices = listOf(0, 1),
                args = messageArgs
            )
        }
        is KidJourneyContentModel.FireNotLit -> {
            // journey_fire_not_lit: 고마워 %s!\n오늘의 조각들이 모두 모였어! %s 을 피워줘!
            StyledMessage(
                messageRes = messageRes,
                highlightIndices = listOf(1),
                args = messageArgs
            )
        }
    }
}

@Composable
private fun StyledMessage(
    messageRes: Int,
    highlightIndices: List<Int>,
    args: Array<Any>
) {
    val rawString = stringResource(messageRes)
    val formattedString = String.format(rawString, *args)

    Text(
        text = buildAnnotatedString {
            var currentIndex = 0
            val pattern = args.joinToString("|") { Regex.escape(it.toString()) }
            val regex = Regex(pattern)

            regex.findAll(formattedString).forEachIndexed { index, matchResult ->
                append(formattedString.substring(currentIndex, matchResult.range.first))

                if (highlightIndices.contains(index)) {
                    withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                        append(matchResult.value)
                    }
                } else {
                    append(matchResult.value)
                }

                currentIndex = matchResult.range.last + 1
            }

            if (currentIndex < formattedString.length) {
                append(formattedString.substring(currentIndex))
            }
        },
        color = KieroTheme.colors.gray300,
        style = KieroTheme.typography.regular.body3
    )
}

@Preview
@Composable
private fun KidJourneyGoblinMessagePreview() {
    KieroTheme {
        KidJourneyGoblinMessage(
            content = KidJourneyContentModel.NowSchedule(
                scheduleName = "피아노 학원 가기",
                stoneType = "용기의 불조각",
                scheduleInfo = KidJourneyScheduleModel(
                    order = 4,
                    startTime = LocalTime.of(14, 0),
                    endTime = LocalTime.of(16, 0)
                ),
                isSkippable = true
            ),
            messageRes = R.string.journey_now_schedule,
            messageArgs = arrayOf("피아노 학원 가기", "용기의 불조각")
        )
    }
}