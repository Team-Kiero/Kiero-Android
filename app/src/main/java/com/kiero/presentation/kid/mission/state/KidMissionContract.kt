package com.kiero.presentation.kid.mission.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.mission.model.KidMissionSectionUiModel
import com.kiero.presentation.kid.mission.model.KidMissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidMissionState(
    val kidName: String = "",
    val kidMissionSectionList: ImmutableList<KidMissionSectionUiModel> = persistentListOf()
) {
    companion object {
        val FAKE = persistentListOf(
            KidMissionSectionUiModel(
                headerTitle = "미션 마감",
                subTitle = "오늘 까지",
                missions = persistentListOf(
                    KidMissionUiModel(
                        id = 1,
                        name = "미션 이름",
                        reward = 2,
                        dueAt = "2023-11-01",
                        isCompleted = false
                    ),
                    KidMissionUiModel(
                        id = 2,
                        name = "미션 이름",
                        reward = 2,
                        dueAt = "2023-11-01",
                        isCompleted = false
                    ),
                    KidMissionUiModel(
                        id = 3,
                        name = "미션 이름",
                        reward = 2,
                        dueAt = "2023-11-02",
                        isCompleted = true
                    )
                )
            )
        )
    }
}