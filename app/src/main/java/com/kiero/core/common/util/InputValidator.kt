package com.kiero.core.common.util

object InputValidator {
    private val NAME_REGEX = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣ]*$")

    fun isValidName(input: String): Boolean {
        return input.matches(NAME_REGEX)
    }
}