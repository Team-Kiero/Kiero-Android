package com.kiero.core.navigation

import kotlinx.serialization.Serializable


@Serializable
data object AuthGraph : Route

@Serializable
data object ParentGraph : Route

@Serializable
data object KidGraph : Route



