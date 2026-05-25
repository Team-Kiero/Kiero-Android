package com.kiero.core.designsystem.component.switches

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroAlarmSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "thumbOffset",
    )

    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> KieroTheme.colors.gray200
            checked -> KieroTheme.colors.main
            else -> KieroTheme.colors.gray800
        },
        animationSpec = tween(200),
    )

    val trackWidth = 44.dp
    val trackHeight = 28.dp
    val thumbSize = 22.dp
    val thumbPadding = (trackHeight - thumbSize) / 2

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .requiredSize(trackWidth, trackHeight)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
            )
            .background(
                color = trackColor,
                shape = RoundedCornerShape(50),
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        val thumbWidth = thumbSize
        val maxOffset = trackWidth - thumbSize - thumbPadding * 2
        val currentOffset = thumbPadding + maxOffset * thumbOffset

        Box(
            modifier = Modifier
                .padding(start = currentOffset)
                .size(width = thumbWidth, height = thumbSize)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(50),
                )
                .background(
                    color = if (checked) KieroTheme.colors.white else KieroTheme.colors.gray200,
                    shape = RoundedCornerShape(50),
                ),
        )
    }
}

@Preview
@Composable
private fun KieroAlarmSwitchPreview() {
    KieroTheme {
        KieroAlarmSwitch(
            checked = true,
            onCheckedChange = {},
            enabled = true,
        )
    }
}
