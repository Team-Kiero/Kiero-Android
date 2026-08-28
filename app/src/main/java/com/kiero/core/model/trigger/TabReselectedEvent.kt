package com.kiero.core.model.trigger

import com.kiero.core.navigation.Route

data class TabReselectedEvent(
    val id: Long,
    val route: Route,
)
