package com.kiero.presentation.main.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kiero.R
import com.kiero.presentation.kid.navigation.Journey
import com.kiero.presentation.kid.navigation.KidTab
import com.kiero.presentation.kid.navigation.Mission
import com.kiero.presentation.kid.navigation.Wish

import com.kiero.presentation.main.navigation.component.BottomBarTab
import com.kiero.presentation.parent.navigation.Alarm
import com.kiero.presentation.parent.navigation.ParentTab
import com.kiero.presentation.parent.navigation.Schedule

enum class ParentMainTab(
    @param:DrawableRes override val iconRes: Int,
    @param:StringRes override val contentDescription: Int,
    @param:StringRes override val labelRes: Int,
    val route: ParentTab,
) : BottomBarTab {
    SCHEDULE(
        iconRes = R.drawable.ic_parent_tab_schedule,
        contentDescription = R.string.schedule_tab_content_description,
        labelRes = R.string.schedule_tab_content_description,
        route = Schedule,
    ),
    ALARM(
        iconRes = R.drawable.ic_parent_tab_alarm,
        contentDescription = R.string.alarm_tab_content_description,
        labelRes = R.string.alarm_tab_content_description,
        route = Alarm,
    );

    companion object {

        fun find(predicate: (ParentTab) -> Boolean): ParentMainTab? {
            return entries.find { predicate(it.route) }
        }

        fun contains(predicate: (ParentTab) -> Boolean): Boolean {
            return entries.any { predicate(it.route) }
        }
    }
}

enum class KidMainTab(
    @param:DrawableRes override val iconRes: Int,
    @param:StringRes override val contentDescription: Int,
    @param:StringRes override val labelRes: Int,
    val route: KidTab,
) : BottomBarTab {
    JOURNEY(
        iconRes = R.drawable.ic_kid_tab_journey,
        contentDescription = R.string.journey_tab_content_description,
        labelRes = R.string.journey_tab_content_description,
        route = Journey,
    ),
    MISSION(
        iconRes = R.drawable.ic_kid_tab_mission,
        contentDescription = R.string.mission_tab_content_description,
        labelRes = R.string.mission_tab_content_description,
        route = Mission,
    ),
    WISH(
        iconRes = R.drawable.ic_kid_tab_wish,
        contentDescription = R.string.wish_tab_content_description,
        labelRes = R.string.wish_tab_content_description,
        route = Wish,
    );

    companion object {

        fun find(predicate: (KidTab) -> Boolean): KidMainTab? {
            return entries.find { predicate(it.route) }
        }

        fun contains(predicate: (KidTab) -> Boolean): Boolean {
            return entries.any { predicate(it.route) }
        }
    }
}