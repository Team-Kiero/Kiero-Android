package com.kiero.presentation.parent.schedule.plan.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.schedule.plan.ParentScheduleAddRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToScheduleAdd(
    initialDate: String,
    isFireLit: Boolean,
    navOptions: NavOptions? = null,
) {
    navigate(ScheduleAdd(initialDate = initialDate, isFireLit = isFireLit), navOptions)
}

fun NavGraphBuilder.parentScheduleAddNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<ScheduleAdd> {
        ParentScheduleAddRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}

// ScheduleAdd navigation data class에 isFireLit 추가
@Serializable
data class ScheduleAdd(
    val initialDate: String,
    val isFireLit: Boolean, // 이 값을 추가로 받습니다.
)