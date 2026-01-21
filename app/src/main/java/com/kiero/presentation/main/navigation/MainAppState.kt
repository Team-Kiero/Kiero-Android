package com.kiero.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.kiero.core.network.monitor.NetworkMonitor
import com.kiero.presentation.auth.navigation.AuthGraph
import com.kiero.presentation.auth.navigation.navigateToAuth
import com.kiero.presentation.auth.parent.navigation.navigateToAuthParent
import com.kiero.presentation.kid.journey.camera.navigation.navigateToCamera
import com.kiero.presentation.kid.journey.fire.navigation.navigateToFire
import com.kiero.presentation.kid.journey.fire.navigation.navigateToFireResult
import com.kiero.presentation.kid.journey.model.StoneUiType
import com.kiero.presentation.kid.journey.navigation.navigateToJourney
import com.kiero.presentation.kid.mission.navigation.navigateToMission
import com.kiero.presentation.kid.navigation.Journey
import com.kiero.presentation.kid.navigation.KidGraph
import com.kiero.presentation.kid.navigation.Mission
import com.kiero.presentation.kid.navigation.Wish
import com.kiero.presentation.kid.onboarding.navigation.navigateToKidOnboarding
import com.kiero.presentation.kid.wish.navigation.navigateToWish
import com.kiero.presentation.parent.alarm.navigation.navigateToAlarm
import com.kiero.presentation.parent.navigation.Alarm
import com.kiero.presentation.parent.navigation.ParentGraph
import com.kiero.presentation.parent.navigation.Schedule
import com.kiero.presentation.parent.schedule.mission.navigation.navigateToMissionAdd
import com.kiero.presentation.parent.schedule.navigation.navigateToSchedule
import com.kiero.presentation.parent.schedule.plan.navigation.navigateToScheduleAdd
import com.kiero.presentation.signup.parent.navigation.navigateToParentSignUp
import com.kiero.presentation.splash.navigation.Splash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@Stable
class MainAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val startDestination = Splash

    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val currentDestination = navController.currentBackStackEntryFlow
        .map { it.destination }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val currentParentTab: StateFlow<ParentMainTab?> = currentDestination
        .map { destination ->
            ParentMainTab.find { tab -> destination?.hasRoute(tab::class) == true }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val currentKidTab: StateFlow<KidMainTab?> = currentDestination
        .map { destination ->
            KidMainTab.find { tab -> destination?.hasRoute(tab::class) == true }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val showParentBottomBar: StateFlow<Boolean> = currentDestination
        .map { destination ->
            ParentMainTab.contains { tab ->
                destination?.hasRoute(tab::class) == true
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val showKidBottomBar: StateFlow<Boolean> = currentDestination
        .map { destination ->
            KidMainTab.contains { tab ->
                destination?.hasRoute(tab::class) == true
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val clearStackNavOptions = navOptions {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }

    fun navigateToParentGraph() = navController.navigate(ParentGraph) {
        popUpTo(AuthGraph) { inclusive = true }
    }

    fun navigateToAuthParent(
        navOptions: NavOptions? = null,
    ) = navController.navigateToAuthParent(
        navOptions = navOptions
    )

    fun navigateToParentSignUp(
        parentName: String,
        parentProfileImage: String,
        navOptions: NavOptions? = null,
    ) = navController.navigateToParentSignUp(
        parentName = parentName,
        parentProfileImage = parentProfileImage,
        navOptions = navOptions
    )

    fun navigateToSelection() = navController.navigateToAuth(
        navOptions = clearStackNavOptions
    )

    fun navigateToKid() {
        navController.navigate(KidGraph) {
            popUpTo(AuthGraph) { inclusive = true }
        }
    }

    fun navigateToAuth() {
        navController.navigate(AuthGraph) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun navigateParentTab(tab: ParentMainTab) {
        val navOptions = navOptions {
            popUpTo(ParentGraph) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
        when (tab) {
            ParentMainTab.SCHEDULE -> navController.navigate(Schedule, navOptions)
            ParentMainTab.ALARM -> navController.navigate(Alarm, navOptions)
        }
    }

    fun navigateKidTab(tab: KidMainTab) {
        val navOptions = navOptions {
            popUpTo(KidGraph) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
        when (tab) {
            KidMainTab.JOURNEY -> navController.navigate(Journey, navOptions)
            KidMainTab.MISSION -> navController.navigate(Mission, navOptions)
            KidMainTab.WISH -> navController.navigate(Wish, navOptions)
        }
    }

    fun navigateToAlarm(navOptions: NavOptions? = null) =
        navController.navigateToAlarm(navOptions)

    fun navigateToSchedule(navOptions: NavOptions? = null) =
        navController.navigateToSchedule(navOptions)

    fun navigateToJourney(navOptions: NavOptions? = null) =
        navController.navigateToJourney(navOptions)

    fun navigateToMission(navOptions: NavOptions? = null) =
        navController.navigateToMission(navOptions)

    fun navigateToWish(navOptions: NavOptions? = null) =
        navController.navigateToWish(navOptions)

    fun navigateToScheduleAdd(
        initialDate: String,
        isFireLit: Boolean,
        navOptions: NavOptions? = null,
    ) = navController.navigateToScheduleAdd(initialDate, isFireLit, navOptions)

    fun navigateToKidOnboarding(navOptions: NavOptions? = null) =
        navController.navigateToKidOnboarding(navOptions)

    fun navigateToCamera(
        scheduleDetailId: Long,
        stoneType: StoneUiType,
        navOptions: NavOptions? = null
    ) = navController.navigateToCamera(
        scheduleDetailId = scheduleDetailId,
        stoneType = stoneType,
        navOptions = navOptions
    )

    fun navigateToFire(
        date: String,
        stones: Int,
        navOptions: NavOptions? = null
    ) = navController.navigateToFire(
        date = date,
        stones = stones,
        navOptions = navOptions
    )

    fun navigateToFireResult(
        date: String,
        navOptions: NavOptions? = null
    ) = navController.navigateToFireResult(
        date = date,
        navOptions = navOptions
    )

    fun navigateToMissionAdd(navOptions: NavOptions? = null) =
        navController.navigateToMissionAdd(navOptions)

    fun navigateUp() {
        navController.navigateUp()
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberMainAppState(
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MainAppState {
    return remember(networkMonitor, navController, coroutineScope) {
        MainAppState(
            networkMonitor = networkMonitor,
            navController = navController,
            coroutineScope = coroutineScope
        )
    }
}
