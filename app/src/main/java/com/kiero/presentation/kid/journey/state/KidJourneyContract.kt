package com.kiero.presentation.kid.journey.state

import androidx.compose.runtime.Immutable
import com.kiero.R
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleModel
import java.time.LocalTime

@Immutable
data class KidJourneyState(
    val isLoading: Boolean = false,
    val header: KidJourneyHeaderModel? = null,
    val content: KidJourneyContentModel = KidJourneyContentModel.NoSchedule
) {

    // 버튼 타입
    val buttonType: KidJourneyButtonType
        get() = when (content) {
            is KidJourneyContentModel.FirstSchedule,
            is KidJourneyContentModel.NowSchedule,
            is KidJourneyContentModel.NextSchedule -> KidJourneyButtonType.AUTH
            is KidJourneyContentModel.FireNotLit -> KidJourneyButtonType.FIRE
            is KidJourneyContentModel.NoSchedule,
            is KidJourneyContentModel.FireLit -> KidJourneyButtonType.NONE
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
            is KidJourneyContentModel.FirstSchedule -> content.isSkippable
            is KidJourneyContentModel.NowSchedule -> content.isSkippable
            is KidJourneyContentModel.NextSchedule -> content.isSkippable
            else -> false
        }

    // 일정 정보 표시 여부
    val shouldShowSchedule: Boolean
        get() = when (content) {
            is KidJourneyContentModel.FirstSchedule,
            is KidJourneyContentModel.NowSchedule,
            is KidJourneyContentModel.NextSchedule -> true
            else -> false
        }

    companion object {
        fun fake() = KidJourneyState(
            header = KidJourneyHeaderModel(
                kidName = "주완",
                currentDate = java.time.LocalDate.of(2024, 12, 5),
                coinCount = 350,
                earnedStones = 5,
                totalScheduleCount = 7
            ),
            content = KidJourneyContentModel.NextSchedule(
                scheduleName = "피아노 학원 가기",
                stoneType = "용기의 불조각",
                scheduleInfo = KidJourneyScheduleModel(
                    order = 1,
                    startTime = LocalTime.of(14, 0),
                    endTime = LocalTime.of(16, 0)
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