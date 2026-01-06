package com.kiero.core.navigation

import kotlinx.serialization.Serializable


@Serializable
data object AuthGraph : Route

@Serializable
data object ParentGraph : Route

@Serializable
data object KidGraph : Route

@Serializable
sealed class Auth : Route {
    @Serializable
    data object Login : Auth()
}

@Serializable
sealed class ParentTab : Route {
    @Serializable
    data object Schedule : ParentTab()

    @Serializable
    data object Alarm : ParentTab()
}

@Serializable
sealed class ParentRoute : Route {
    // TODO : PARENT 화면 내 세부화면 Route 설정
}

@Serializable
sealed class KidTab : Route {
    @Serializable
    data object Journey : KidTab()

    @Serializable
    data object Mission : KidTab()

    @Serializable
    data object Wish : KidTab()
}

@Serializable
sealed class KidRoute : Route {
// TODO : KID 화면 내 세부화면 Route 설정
}