package com.kiero.presentation.kid.wish.model

import androidx.compose.runtime.Immutable

@Immutable
data class KidWishMission(
    val id: Int = -1,
    val name: String = "", // 미션 이름
    val reward: Int = -1,
    val dueAt: String = "",
    val isCompleted: Boolean = false
)