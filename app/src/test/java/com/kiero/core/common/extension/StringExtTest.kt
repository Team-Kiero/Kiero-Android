package com.kiero.core.common.extension

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtTest {
    @Test
    fun `https URL은 그대로 유지한다`() {
        val url = "https://trusted.example/path"

        assertEquals(url, url.toSafeHttpsUrl())
    }

    @Test
    fun `스킴이 없는 URL에는 https를 추가한다`() {
        assertEquals(
            "https://trusted.example/path",
            "trusted.example/path".toSafeHttpsUrl()
        )
    }

    @Test
    fun `h가 누락된 ttps URL을 https로 복구한다`() {
        assertEquals(
            "https://trusted.example/path",
            "ttps://trusted.example/path".toSafeHttpsUrl()
        )
    }

    @Test
    fun `허용 목록의 도메인만 연다`() {
        val allowedHosts = setOf("trusted.example")
        val url = "https://trusted.example/path"

        assertEquals(url, url.toTrustedHttpsUrl(allowedHosts))
        assertEquals(null, "https://attacker.example/path".toTrustedHttpsUrl(allowedHosts))
    }

    @Test
    fun `유사 도메인과 사용자 정보 우회 URL을 차단한다`() {
        val allowedHosts = setOf("trusted.example")

        assertEquals(
            "https://trusted.example/path",
            "trusted.example/path".toTrustedHttpsUrl(allowedHosts)
        )
        assertEquals(null, "https://trusted.example.attacker.example/path".toTrustedHttpsUrl(allowedHosts))
        assertEquals(null, "https://trusted.example@attacker.example/path".toTrustedHttpsUrl(allowedHosts))
    }

    @Test
    fun `허용된 도메인의 하위 도메인을 연다`() {
        assertEquals(
            "https://trusted.example/path",
            "trusted.example/path".toTrustedHttpsUrl(
                allowedHostSuffixes = setOf("trusted.example")
            )
        )
        assertEquals(
            "https://workspace.trusted.example/path",
            "workspace.trusted.example/path".toTrustedHttpsUrl(
                allowedHostSuffixes = setOf("trusted.example")
            )
        )
        assertEquals(
            null,
            "https://workspace.trusted.example.attacker.example/path".toTrustedHttpsUrl(
                allowedHostSuffixes = setOf("trusted.example")
            )
        )
    }
}
