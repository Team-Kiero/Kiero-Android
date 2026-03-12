package com.kiero.presentation.kid.journey.state

import androidx.compose.runtime.Immutable
import com.kiero.R
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel
import com.kiero.presentation.kid.journey.model.StoneUiType

@Immutable
data class KidJourneyState(
    val header: KidJourneyHeaderUiModel? = null,
    val content: KidJourneyContentUiModel = KidJourneyContentUiModel.NoSchedule
) {

    // 버튼 타입
    val buttonType: KidJourneyButtonType
        get() = when (content) {
            is KidJourneyContentUiModel.ScheduledContent -> {
                if (content.isNowScheduleVerified) {
                    KidJourneyButtonType.NONE
                } else {
                    KidJourneyButtonType.AUTH
                }
            }
            is KidJourneyContentUiModel.FireNotLit -> KidJourneyButtonType.FIRE
            else -> KidJourneyButtonType.NONE
        }

    // 버튼 텍스트 리소스 ID
    val buttonTextRes: Int?
        get() = when (buttonType) {
            KidJourneyButtonType.AUTH -> R.string.journey_btn_auth
            KidJourneyButtonType.FIRE -> R.string.journey_btn_fire
            KidJourneyButtonType.NONE -> null
        }

    // 다음 일정 버튼 활성화
    val shouldShowNextButton: Boolean
        get() = (content as? KidJourneyContentUiModel.ScheduledContent)?.isSkippable ?: false

    // 일정 정보 표시 여부
    val shouldShowSchedule: Boolean
        get() = content is KidJourneyContentUiModel.ScheduledContent

    //  스케줄 정보
    val currentScheduleInfo: KidJourneyScheduleUiModel?
        get() = (content as? KidJourneyContentUiModel.ScheduledContent)?.scheduleInfo

    companion object {
        fun fake() = KidJourneyState(
            header = KidJourneyHeaderUiModel(
                kidName = "민성",
                currentDate = "1월 5일 목요일",
                coinCount = 350,
                earnedStones = 4,
                totalScheduleCount = 7
            ),
            content = KidJourneyContentUiModel.NowSchedule(
                scheduleDetailId = 1,
                scheduleName = "피아노 학원 가기",
                stoneType = StoneUiType.WISDOM,
                scheduleInfo = KidJourneyScheduleUiModel(
                    order = 1,
                    startTime = "14:00:00",
                    endTime = "16:00:00"
                ),
                isSkippable = true,
                isNowScheduleVerified = true
            )
        )
    }
}

sealed interface KidJourneySideEffect {
    data object ShowDialog : KidJourneySideEffect

    data class ShowToast(
        val message: String
    ) : KidJourneySideEffect

    data class ShowSnackbar(
        val message: String,
    ) : KidJourneySideEffect
}