package com.kiero.core.common.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

fun Modifier.forcePixelToDp(painter: Painter): Modifier {
    return this.size(
        width = painter.intrinsicSize.width.dp,
        height = painter.intrinsicSize.height.dp
    )
}