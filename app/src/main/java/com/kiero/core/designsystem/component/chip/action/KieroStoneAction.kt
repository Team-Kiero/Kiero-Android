package com.kiero.core.designsystem.component.chip.action

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

class KieroStoneAction(
    private val currentStoneCount: Int,
    private val maxStoneCount: Int,
    private val isEnabled: Boolean = true,
    private val onClick: () -> Unit
) : ChipAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        val stone = painterResource(id = R.drawable.img_kid_journey_stone_blue)

        val isCompleted = currentStoneCount == maxStoneCount

        val targetColor = when {
            !isEnabled -> KieroTheme.colors.gray500
            isCompleted -> KieroTheme.colors.main
            else -> KieroTheme.colors.gray100
        }

        Row(
            modifier = modifier
                .noRippleClickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = stone,
                contentDescription = null,
                modifier = Modifier
                    .size(
                        width = 19.dp,
                        height = 23.dp
                    ),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "$currentStoneCount / $maxStoneCount 개",
                style = KieroTheme.typography.regular.body3,
                color = targetColor
            )
        }
    }
}

@Preview
@Composable
private fun KieroStoneActionPreview() {
    KieroTheme {
        KieroStoneAction(
            maxStoneCount = 10,
            currentStoneCount = 10,
            onClick = {},
        ).invoke(modifier = Modifier)
    }
}