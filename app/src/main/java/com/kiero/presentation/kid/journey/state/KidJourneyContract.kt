package com.kiero.presentation.kid.journey.state

import androidx.compose.runtime.Immutable
import com.kiero.R
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderModel

@Immutable
data class KidJourneyState(
    val isLoading: Boolean = false,
    val header: KidJourneyHeaderModel? = null,
    val content: KidJourneyContentModel = KidJourneyContentModel.NoSchedule
) {
    // 꾸비 메시지 리소스 ID
    val goblinMessageRes: Int
        get() = when (content) {
            is KidJourneyContentModel.NoSchedule -> R.string.journey_no_schedule
            is KidJourneyContentModel.FirstSchedule -> R.string.journey_first_schedule
            is KidJourneyContentModel.NowSchedule -> R.string.journey_now_schedule
            is KidJourneyContentModel.NextSchedule -> R.string.journey_next_schedule
            is KidJourneyContentModel.FireNotLit -> R.string.journey_fire_not_lit
            is KidJourneyContentModel.FireLit -> R.string.journey_fire_lit
        }

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

    // 메시지에 들어갈 인자 생성
    fun getMessageArgs(): Array<Any> {
        return when (content) {
            is KidJourneyContentModel.FirstSchedule -> {
                arrayOf(content.scheduleName)
            }
            is KidJourneyContentModel.NowSchedule -> {
                val nowContent = content
                arrayOf(nowContent.scheduleName, nowContent.stoneType)
            }
            is KidJourneyContentModel.NextSchedule -> {
                val nextContent = content
                arrayOf(nextContent.scheduleName, nextContent.stoneType)
            }
            is KidJourneyContentModel.FireNotLit -> {
                arrayOf(
                    content.kidName,
                    "영웅의 불꽃"
                )
            }
            is KidJourneyContentModel.NoSchedule,
            is KidJourneyContentModel.FireLit -> {
                emptyArray()
            }
        }
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