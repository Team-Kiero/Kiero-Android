package com.kiero.presentation.main.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kiero.R
import com.kiero.presentation.kid.navigation.KidJourney
import com.kiero.presentation.kid.navigation.KidMission
import com.kiero.presentation.kid.navigation.KidTab
import com.kiero.presentation.kid.navigation.KidWish
import com.kiero.presentation.main.navigation.component.BottomBarTab
import com.kiero.presentation.parent.navigation.ParentJourney
import com.kiero.presentation.parent.navigation.ParentMission
import com.kiero.presentation.parent.navigation.ParentMypage
import com.kiero.presentation.parent.navigation.ParentReward
import com.kiero.presentation.parent.navigation.ParentSchedule
import com.kiero.presentation.parent.navigation.ParentTab

enum class ParentMainTab(
    @param:DrawableRes override val iconRes: Int,
    @param:StringRes override val contentDescription: Int,
    @param:StringRes override val labelRes: Int,
    val route: ParentTab,
) : BottomBarTab {

    JOURNEY(
        iconRes = R.drawable.ic_parent_tab_today,
        contentDescription = R.string.parent_journey_tab_content_description,
        labelRes = R.string.parent_journey_tab_content_description,
        route = ParentJourney,
    ),
    SCHEDULE(
        iconRes = R.drawable.ic_parent_tab_schedule,
        contentDescription = R.string.schedule_tab_content_description,
        labelRes = R.string.schedule_tab_content_description,
        route = ParentSchedule,
    ),

    MISSION(
        iconRes = R.drawable.ic_parent_tab_mission,
        contentDescription = R.string.parent_mission_tab_content_description,
        labelRes = R.string.parent_mission_tab_content_description,
        route = ParentMission,
    ),

    REWARD(
        iconRes = R.drawable.ic_parent_tab_reward,
        contentDescription = R.string.reward_tab_content_description,
        labelRes = R.string.reward_tab_content_description,
        route = ParentReward,
    ),
    MYPAGE(
        iconRes = R.drawable.ic_parent_tab_mypage,
        contentDescription = R.string.mypage_tab_content_description,
        labelRes = R.string.mypage_tab_content_description,
        route = ParentMypage,
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
        contentDescription = R.string.kid_journey_tab_content_description,
        labelRes = R.string.kid_journey_tab_content_description,
        route = KidJourney,
    ),
    MISSION(
        iconRes = R.drawable.ic_kid_tab_mission,
        contentDescription = R.string.kid_mission_tab_content_description,
        labelRes = R.string.kid_mission_tab_content_description,
        route = KidMission,
    ),
    WISH(
        iconRes = R.drawable.ic_kid_tab_wish,
        contentDescription = R.string.wish_tab_content_description,
        labelRes = R.string.wish_tab_content_description,
        route = KidWish,
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