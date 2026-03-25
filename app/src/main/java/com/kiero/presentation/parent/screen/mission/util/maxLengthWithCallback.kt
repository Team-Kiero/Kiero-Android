package com.kiero.presentation.parent.screen.mission.util

import androidx.compose.foundation.text.input.InputTransformation

// 공통으로 써야할 수도 있음
fun maxLengthWithCallback(
    maxLength: Int,
    onMaxLengthReached: () -> Unit
): InputTransformation = InputTransformation {
    // 변경 후 길이가 maxLength 초과면
    if (asCharSequence().length > maxLength) {
        // 초과분 잘라내기
        replace(maxLength, asCharSequence().length, "")
        onMaxLengthReached()
    }
}