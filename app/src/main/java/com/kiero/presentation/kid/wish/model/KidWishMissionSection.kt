package com.kiero.presentation.kid.wish.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidWishMissionSection(
    val headerTitle: String = "미션 마감",
    val subTitle: String = "",
    val missions: PersistentList<KidWishMission> = persistentListOf()
)