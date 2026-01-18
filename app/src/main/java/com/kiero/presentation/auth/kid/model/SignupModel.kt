package com.kiero.presentation.auth.kid.model


data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object ValidationRules {

    fun validateNoSpecialChars(text: String): ValidationResult {
        val regex = "^[a-zA-Z0-9가-힣]*$".toRegex()
        return if (text.matches(regex)) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "특수문자는 사용할 수 없습니다")
        }
    }

    fun validateNoSpaces(text: String): ValidationResult {
        return if (!text.contains(" ")) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "공백은 사용할 수 없습니다")
        }
    }

    fun validateNotEmpty(text: String): ValidationResult {
        return if (text.isNotBlank()) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "필수 입력 항목입니다")
        }
    }

    fun validateLength(text: String, minLength: Int, maxLength: Int): ValidationResult {
        return when {
            text.length < minLength ->
                ValidationResult(false, "최소 ${minLength}자 이상 입력해주세요")
            text.length > maxLength ->
                ValidationResult(false, "최대 ${maxLength}자까지 입력 가능합니다")
            else -> ValidationResult(true)
        }
    }

    fun validateAll(text: String, vararg validators: (String) -> ValidationResult): ValidationResult {
        validators.forEach { validator ->
            val result = validator(text)
            if (!result.isValid) return result
        }
        return ValidationResult(true)
    }
}