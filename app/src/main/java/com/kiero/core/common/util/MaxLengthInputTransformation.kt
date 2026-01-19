package com.kiero.core.common.util

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.delete

data class MaxLengthInputTransformation(
    private val limit: Int
) : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        var i = 0
        while (i < length) {
            if (asCharSequence()[i].isWhitespace()) {
                delete(i, i + 1)
            } else {
                i++
            }
        }

        if (length > limit) {
            delete(limit, length)
        }
    }
}