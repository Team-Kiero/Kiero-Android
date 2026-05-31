package com.kiero.presentation.kid.journey.state

import androidx.compose.runtime.Immutable
import com.kiero.R
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

@Immutable
data class KidJourneyState(
    val header: KidJourneyHeaderUiModel? = null,
    val content: KidJourneyContentUiModel = KidJourneyContentUiModel.NoSchedule,
    val permissionCameraDeniedCount: Int = 0,
    val permissionNotificationDeniedCount: Int = 0,
    val showNotificationPermissionDialog: Boolean = false,
) {
    val buttonType: KidJourneyButtonType
        get() = when (content) {
            is KidJourneyContentUiModel.ScheduledContent -> {
                when {
                    content.isNowScheduleVerified -> KidJourneyButtonType.NONE
                    else -> KidJourneyButtonType.AUTH
                }
            }
            is KidJourneyContentUiModel.FireNotLit -> KidJourneyButtonType.FIRE
            else -> KidJourneyButtonType.NONE
        }

    val buttonTextRes: Int?
        get() = when (buttonType) {
            KidJourneyButtonType.AUTH -> R.string.journey_btn_auth
            KidJourneyButtonType.FIRE -> R.string.journey_btn_fire
            KidJourneyButtonType.NONE -> null
        }

    val shouldShowNextButton: Boolean
        get() = (content as? KidJourneyContentUiModel.ScheduledContent)?.isSkippable ?: false

    val shouldShowFinishButton: Boolean
        get() {
            val scheduledContent = content as? KidJourneyContentUiModel.ScheduledContent ?: return false
            val h = header ?: return false
            return !scheduledContent.isSkippable
                    && h.totalScheduleCount != null
                    && h.totalScheduleCount > 0
                    && scheduledContent.scheduleInfo.order == h.totalScheduleCount
        }

    val shouldShowSchedule: Boolean
        get() = content is KidJourneyContentUiModel.ScheduledContent

    val currentScheduleInfo: KidJourneyScheduleUiModel?
        get() = (content as? KidJourneyContentUiModel.ScheduledContent)?.scheduleInfo

    companion object {
        val FAKE = KidJourneyState(
            header = KidJourneyHeaderUiModel(
                kidName = "주완",
                currentDate = "12월 5일 목요일",
                coinCount = 350,
                earnedStones = 5,
                totalScheduleCount = 7
            ),
            content = KidJourneyContentUiModel.NowSchedule(
                scheduleDetailId = 1,
                scheduleName = "피아노 학원 가기",
                stoneType = KidJourneyStoneType.WISDOM,
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