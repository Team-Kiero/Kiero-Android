package com.kiero.presentation.splash.component

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.R
import com.kiero.core.common.extension.forcePixelToDp

@Composable
fun SplashLightFloating(
    modifier: Modifier = Modifier
) {
    val lightPainter = painterResource(id = R.drawable.img_splash_light)
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
        painter = lightPainter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .forcePixelToDp(lightPainter)
            .graphicsLayer {
                this.translationY = floatAnim
            }
    )
}

@Preview
@Composable
private fun StoneFloatingPreview() {
    SplashLightFloating()
}