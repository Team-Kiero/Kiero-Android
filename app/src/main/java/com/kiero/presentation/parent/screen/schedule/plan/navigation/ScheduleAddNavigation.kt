package com.kiero.presentation.parent.screen.schedule.plan.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.screen.schedule.plan.ParentScheduleAddRoute
import kotlinx.serialization.Serializable


@Serializable
data class ScheduleAdd(
    val initialDate: String,
    val isFireLit: Boolean,
)

@Serializable
data class ScheduleEdit(
    val scheduleId: Long,
    val selectedDate: String,
    val name: String,
    val isRecurring: Boolean,
    val startTime: String,
    val endTime: String,
    val scheduleColor: String,
    val dayOfWeek: String?,
    val dates: String?,
    val isIncludeFollowing: Boolean? = null,
)

fun NavController.navigateToScheduleAdd(
    initialDate: String,
    isFireLit: Boolean,
    navOptions: NavOptions? = null,
) {
    navigate(ScheduleAdd(initialDate = initialDate, isFireLit = isFireLit), navOptions)
}

fun NavController.navigateToScheduleEdit(
    args: ScheduleEdit,
    navOptions: NavOptions? = null,
) {
    navigate(args, navOptions)
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

    composable<ScheduleEdit> {
        ParentScheduleAddRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}