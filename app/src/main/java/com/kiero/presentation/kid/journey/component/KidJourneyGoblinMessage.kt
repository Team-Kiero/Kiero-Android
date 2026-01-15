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
import com.kiero.presentation.kid.component.KidSpeechField
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
            MultiLineStyledMessage(
                messageRes = messageRes,
                highlightIndices = listOf(0),
                args = messageArgs
            )
        }
        is KidJourneyContentModel.NowSchedule,
        is KidJourneyContentModel.NextSchedule -> {
            MultiLineStyledMessage(
                messageRes = messageRes,
                highlightIndices = listOf(0, 1),
                args = messageArgs
            )
        }
        is KidJourneyContentModel.FireNotLit -> {
            MultiLineStyledMessage(
                messageRes = messageRes,
                highlightIndices = listOf(1),
                args = messageArgs
            )
        }
    }
}

@Composable
private fun MultiLineStyledMessage(
    messageRes: Int,
    highlightIndices: List<Int>,
    args: Array<Any>
) {
    val rawString = stringResource(messageRes)
    val formattedString = String.format(rawString, *args)
    val lines = formattedString.split("\n")

    lines.forEach { line ->
        StyledMessage(
            text = line,
            highlightIndices = highlightIndices,
            args = args
        )
    }
}

@Composable
private fun StyledMessage(
    text: String,
    highlightIndices: List<Int>,
    args: Array<Any>
) {
    Text(
        text = buildAnnotatedString {
            var currentIndex = 0
            val pattern = args.joinToString("|") { Regex.escape(it.toString()) }
            val regex = Regex(pattern)

            regex.findAll(text).forEachIndexed { index, matchResult ->
                append(text.substring(currentIndex, matchResult.range.first))

                if (highlightIndices.contains(index)) {
                    withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                        append(matchResult.value)
                    }
                } else {
                    append(matchResult.value)
                }

                currentIndex = matchResult.range.last + 1
            }

            if (currentIndex < text.length) {
                append(text.substring(currentIndex))
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
        KidSpeechField(
            name = "꾸비"
        ) {
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
}