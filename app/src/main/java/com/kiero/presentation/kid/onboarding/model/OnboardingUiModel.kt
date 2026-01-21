package com.kiero.presentation.kid.onboarding.model

import com.kiero.R


enum class OnboardingUiModel (
    val step : Int,
    val backImage : Int,
    val description : String
){
    STORY1(1, R.drawable.img_kid_onboarding_0,"Story 1"),
    STORY2(2,R.drawable.img_kid_onboarding_1,"Story 2"),
    STORY3(3, R.drawable.img_kid_onboarding_2,"Story 3"),
    STORY4(4,R.drawable.img_kid_onboarding_3,"Story 4"),
    STORY5(5,R.drawable.img_kid_onboarding_4,"Story 5")
}
