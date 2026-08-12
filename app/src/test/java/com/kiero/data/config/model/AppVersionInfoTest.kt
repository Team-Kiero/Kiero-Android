package com.kiero.data.config.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppVersionInfoTest {

    @Test
    fun `higher minimum force version returns FORCE`() {
        val versionInfo = AppVersionInfo(
            minForceVersion = AppVersion("2.0.0"),
            latestVersion = AppVersion("2.1.0"),
        )

        assertEquals(UpdateState.FORCE, versionInfo.checkUpdateState(AppVersion("1.9.9")))
    }

    @Test
    fun `higher latest version returns FLEXIBLE`() {
        val versionInfo = AppVersionInfo(
            minForceVersion = AppVersion("2.0.0"),
            latestVersion = AppVersion("2.1.0"),
        )

        assertEquals(UpdateState.FLEXIBLE, versionInfo.checkUpdateState(AppVersion("2.0.0")))
    }

    @Test
    fun `same or newer version returns NONE`() {
        val versionInfo = AppVersionInfo(
            minForceVersion = AppVersion("2.0.0"),
            latestVersion = AppVersion("2.1.0"),
        )

        assertEquals(UpdateState.NONE, versionInfo.checkUpdateState(AppVersion("2.1.0")))
        assertEquals(UpdateState.NONE, versionInfo.checkUpdateState(AppVersion("2.2.0")))
    }

    @Test
    fun `version components are compared without fixed-width collisions`() {
        assertTrue(AppVersion("1.1000.0") < AppVersion("2.0.0"))
        assertTrue(AppVersion("1.0.1000") > AppVersion("1.0.2"))
        assertEquals(AppVersion("1.0.0"), AppVersion("1.0"))
    }
}
