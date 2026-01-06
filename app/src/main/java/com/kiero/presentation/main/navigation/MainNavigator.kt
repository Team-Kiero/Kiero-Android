package com.kiero.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.kiero.core.navigation.AuthGraph
import com.kiero.core.navigation.KidGraph
import com.kiero.core.navigation.KidTab
import com.kiero.core.navigation.ParentGraph
import com.kiero.core.navigation.ParentTab
import com.kiero.presentation.kid.journey.navigateToJourney
import com.kiero.presentation.kid.mission.navigateToMission
import com.kiero.presentation.kid.wish.navigateToWish
import com.kiero.presentation.parent.alarm.navigation.navigateToAlarm
import com.kiero.presentation.parent.schedule.navigation.navigateToSchedule


class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState()
            .value?.destination

    val currentParentTab: ParentMainTab?
        @Composable get() = ParentMainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    val currentKidTab: KidMainTab?
        @Composable get() = KidMainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigateToParent() {
        navController.navigate(ParentGraph) {
            popUpTo(AuthGraph) {
                inclusive = true
            }
        }
    }

    fun navigateToKid() {
        navController.navigate(KidGraph) {
            popUpTo(AuthGraph) {
                inclusive = true
            }
        }
    }

    fun navigateToAuth() {
        navController.navigate(AuthGraph) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }


    fun navigateParentTab(tab: ParentMainTab) {
        val navOptions = navOptions {
            popUpTo(ParentGraph) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            ParentMainTab.SCHEDULE -> navController.navigate(ParentTab.Schedule, navOptions)
            ParentMainTab.ALARM -> navController.navigate(ParentTab.Alarm, navOptions)
        }
    }

    fun navigateKidTab(tab: KidMainTab) {
        val navOptions = navOptions {
            popUpTo(KidGraph) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            KidMainTab.JOURNEY -> navController.navigate(KidTab.Journey, navOptions)
            KidMainTab.MISSION -> navController.navigate(KidTab.Mission, navOptions)
            KidMainTab.WISH -> navController.navigate(KidTab.Wish, navOptions)
        }
    }


    fun navigateToAlarm(navOptions: NavOptions? = null) {
        navController.navigateToAlarm(navOptions = navOptions)
    }

    fun navigateToSchedule(navOptions: NavOptions? = null) {
        navController.navigateToSchedule(navOptions = navOptions)
    }

    fun navigateToJourney(navOptions: NavOptions? = null) {
        navController.navigateToJourney(navOptions = navOptions)
    }

    fun navigateToMission(navOptions: NavOptions? = null) {
        navController.navigateToMission(navOptions = navOptions)
    }

    fun navigateToWish(navOptions: NavOptions? = null) {
        navController.navigateToWish(navOptions = navOptions)
    }


    fun navigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    @Composable
    fun showParentBottomBar(): Boolean {
        return currentDestination?.hasRoute(ParentTab.Schedule::class) == true ||
                currentDestination?.hasRoute(ParentTab.Alarm::class) == true
    }

    @Composable
    fun showKidBottomBar(): Boolean {
        return currentDestination?.hasRoute(KidTab.Journey::class) == true ||
                currentDestination?.hasRoute(KidTab.Mission::class) == true ||
                currentDestination?.hasRoute(KidTab.Wish::class) == true
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}