package com.kiero.core.localstorage.onboarding

interface OnboardingManager {
    suspend fun saveIsSawOnboarding(isSaw: Boolean)
    suspend fun getIsSawOnboarding(): Boolean
}