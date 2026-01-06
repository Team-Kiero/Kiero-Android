package com.kiero.presentation.main.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.kiero.R
import com.kiero.core.navigation.KidTab
import com.kiero.core.navigation.ParentTab
import com.kiero.presentation.main.navigation.component.BottomBarTab

enum class ParentMainTab(
    @DrawableRes override val iconRes: Int,
    @StringRes override val contentDescription: Int,
    @StringRes override val labelRes: Int,
    val route: ParentTab,
) : BottomBarTab {
    SCHEDULE(
        iconRes = R.drawable.ic_parent_tab_schedule,
        contentDescription = R.string.schedule_tab_content_desription,
        labelRes = R.string.schedule_tab_content_desription,
        route = ParentTab.Schedule,
    ),
    ALARM(
        iconRes = R.drawable.ic_parent_tab_alarm,
        contentDescription = R.string.alarm_tab_content_desription,
        labelRes = R.string.alarm_tab_content_desription,
        route = ParentTab.Alarm,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (ParentTab) -> Boolean): ParentMainTab? {
            return entries.find { predicate(it.route) }
        }
    }
}

enum class KidMainTab(
    @DrawableRes override val iconRes: Int,
    @StringRes override val contentDescription: Int,
    @StringRes override val labelRes: Int,
    val route: KidTab,
) : BottomBarTab {
    JOURNEY(
        iconRes = R.drawable.ic_kid_tab_journey,
        contentDescription = R.string.journey_tab_content_description,
        labelRes = R.string.journey_tab_content_description,
        route = KidTab.Journey,
    ),
    MISSION(
        iconRes = R.drawable.ic_kid_tab_mission,
        contentDescription = R.string.mission_tab_content_description,
        labelRes = R.string.mission_tab_content_description,
        route = KidTab.Mission,
    ),
    WISH(
        iconRes = R.drawable.ic_kid_tab_wish,
        contentDescription = R.string.wish_tab_content_description,
        labelRes = R.string.wish_tab_content_description,
        route = KidTab.Wish,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (KidTab) -> Boolean): KidMainTab? {
            return entries.find { predicate(it.route) }
        }
    }
}