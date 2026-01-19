package com.kiero.presentation.kid.journey.state

import androidx.compose.runtime.Immutable
import com.kiero.R
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel

@Immutable
data class KidJourneyState(
    val isLoading: Boolean = false,
    val header: KidJourneyHeaderUiModel? = null,
    val content: KidJourneyContentUiModel = KidJourneyContentUiModel.NoSchedule
) {

    // 버튼 타입
    val buttonType: KidJourneyButtonType
        get() = when (content) {
            is KidJourneyContentUiModel.FirstSchedule,
            is KidJourneyContentUiModel.NowSchedule,
            is KidJourneyContentUiModel.NextSchedule -> KidJourneyButtonType.AUTH
            is KidJourneyContentUiModel.FireNotLit -> KidJourneyButtonType.FIRE
            is KidJourneyContentUiModel.NoSchedule,
            is KidJourneyContentUiModel.FireLit -> KidJourneyButtonType.NONE
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
        get() = when (content) {
            is KidJourneyContentUiModel.FirstSchedule -> content.isSkippable
            is KidJourneyContentUiModel.NowSchedule -> content.isSkippable
            is KidJourneyContentUiModel.NextSchedule -> content.isSkippable
            else -> false
        }

    // 일정 정보 표시 여부
    val shouldShowSchedule: Boolean
        get() = when (content) {
            is KidJourneyContentUiModel.FirstSchedule,
            is KidJourneyContentUiModel.NowSchedule,
            is KidJourneyContentUiModel.NextSchedule -> true
            else -> false
        }

    companion object {
        fun fake() = KidJourneyState(
            header = KidJourneyHeaderUiModel(
                kidName = "주완",
                currentDate = "12월 5일 목요일",
                coinCount = 350,
                earnedStones = 7,
                totalScheduleCount = 7
            ),
            content = KidJourneyContentUiModel.NextSchedule(
                scheduleDetailId = 1,
                scheduleName = "피아노 학원 가기",
                stoneType = "용기의 불조각",
                scheduleInfo = KidJourneyScheduleUiModel(
                    order = 1,
                    startTime = "14:00:00",
                    endTime = "16:00:00"
                ),
                isSkippable = true
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