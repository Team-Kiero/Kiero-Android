package com.kiero.presentation.kid.journey.camera.component

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.R

@Composable
fun KidCameraStoneFloating(
    stoneImageRes: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                delayMillis = 100,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset"
    )

    Image(
        painter = painterResource(id = stoneImageRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.translationY = floatAnim
            }
    )
}

@Preview
@Composable
private fun StoneFloatingPreview() {
    KidCameraStoneFloating(
        stoneImageRes = R.drawable.img_kid_journey_stone_blue
    )
}