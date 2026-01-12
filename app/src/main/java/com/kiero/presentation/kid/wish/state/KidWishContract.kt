package com.kiero.presentation.kid.wish.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.wish.model.KidWishMission
import com.kiero.presentation.kid.wish.model.KidWishMissionSection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidWishState(
    val kidName: String = "",
    val kidMissionSectionList: ImmutableList<KidWishMissionSection> = persistentListOf()
) {
    companion object {
        val FAKE = persistentListOf(
            KidWishMissionSection(
                headerTitle = "미션 마감",
                subTitle = "오늘 까지",
                missions = persistentListOf(
                    KidWishMission(
                        id = 1,
                        name = "미션 이름",
                        reward = 2,
                        dueAt = "2023-11-01",
                        isCompleted = false
                    ),
                    KidWishMission(
                        id = 2,
                        name = "미션 이름",
                        reward = 2,
                        dueAt = "2023-11-01",
                        isCompleted = false
                    ),
                    KidWishMission(
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