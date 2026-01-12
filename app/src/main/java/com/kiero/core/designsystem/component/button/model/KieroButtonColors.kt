package com.kiero.core.designsystem.component.button.model


import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.kiero.core.designsystem.theme.Black
import com.kiero.core.designsystem.theme.Main

/**
 * 버튼의 배경색, 컨텐츠색, 경계색을 정의하는 클래스
 *
 * @param contentColor 내부 컴포저블(텍스트) 색상
 * @param containerColor 배경 색상
 */

@Immutable
open class KieroButtonColors(
    private val contentColor: Color = Black,
    private val containerColor: Color = Main,
) {
    @Stable
    internal fun contentColor() = contentColor

    @Stable
    internal fun containerColor() = containerColor
}