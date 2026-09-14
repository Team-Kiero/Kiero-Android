package com.kiero.core.common.state

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Stable

/**
 * [min]~[max] 범위를 갖는 숫자 입력 필드의 상태 홀더.
 *
 * TextFieldState 와 그 필드에 필요한 보정 규칙을 한 곳에 묶어서,
 * ViewModel 이 필드마다 같은 클램핑 코드를 반복하지 않도록 한다.
 * ViewModel 이 소유하고 화면에는 [textState] 를 그대로 내려보낸다.
 *
 * 입력 시점 제한(자릿수·숫자 여부)과 타이핑 중 보정은 기존과 동일하게
 * 각 화면의 InputTransformation 과 ViewModel 의 snapshotFlow 가 그대로 담당한다.
 */
@Stable
class ClampedNumberFieldState(
    private val min: Int,
    private val max: Int,
    initialValue: Int? = null,
    /** 필드가 비어 있을 때 [applyChange]·[exceedsMaxAfter] 가 기준으로 삼는 값. */
    private val emptyValue: Int = 0,
) {
    val textState = TextFieldState(initialValue?.toString().orEmpty())

    val text: String
        get() = textState.text.toString()

    /** 현재 입력값. 비어 있으면 null. */
    val value: Int?
        get() = text.toIntOrNull()

    /**
     * 포커스 아웃·제출 시점에 [min]~[max] 로 보정하고 최종값을 돌려준다.
     * 이미 범위 안이면 텍스트를 건드리지 않는다.
     */
    fun clampToRange(): Int {
        val current = value
        val clamped = (current ?: min).coerceIn(min, max)
        if (current == null || current != clamped) {
            setValue(clamped)
        }
        return clamped
    }

    /** 프리셋 버튼(+5, -10 …)으로 값을 더하고 보정된 최종값을 돌려준다. */
    fun applyChange(change: Int): Int {
        val updated = ((value ?: emptyValue) + change).coerceIn(min, max)
        setValue(updated)
        return updated
    }

    /** [change] 를 적용했을 때 상한을 넘는지. 스낵바 노출 여부 판단용. */
    fun exceedsMaxAfter(change: Int): Boolean = (value ?: emptyValue) + change > max

    fun setValue(value: Int) {
        textState.setTextAndPlaceCursorAtEnd(value.toString())
    }
}
