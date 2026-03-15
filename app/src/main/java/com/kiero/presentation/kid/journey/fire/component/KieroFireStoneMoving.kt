package com.kiero.presentation.kid.journey.fire.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

@Composable
fun KieroFireStoneMoving(
    stoneRes: Int,
    modifier: Modifier = Modifier
) {
    var currentState by remember { mutableStateOf(StoneAnimationState.Invisible) }

    val transition = updateTransition(targetState = currentState, label = "StoneTransition")

    LaunchedEffect(Unit) {
        delay(1500)
        currentState = StoneAnimationState.Initial
        currentState = StoneAnimationState.Target
    }

    val alphaAnim by transition.animateFloat(
        transitionSpec = { tween(1000) },
        label = "alpha"
    ) { state ->
        when (state) {
            StoneAnimationState.Invisible -> 0f
            StoneAnimationState.Initial -> 1f
            StoneAnimationState.Target -> 1f
        }
    }

    val rotateAnim by transition.animateFloat(
        transitionSpec = { tween(1000) },
        label = "rotation"
    ) { state ->
        when (state) {
            StoneAnimationState.Invisible -> - 30f
            StoneAnimationState.Initial -> -30f
            StoneAnimationState.Target -> 0f
        }
    }

    val translateAnim by transition.animateFloat(
        transitionSpec = { tween(1000) },
        label = "translationY"
    ) { state ->
        when (state) {
            StoneAnimationState.Invisible -> 0f
            StoneAnimationState.Initial -> 0f
            StoneAnimationState.Target -> -100f
        }
    }

    Image(
        painter = painterResource(id = stoneRes),
        contentDescription = null,
        modifier = modifier
            .graphicsLayer {
                alpha = alphaAnim
                rotationZ = rotateAnim
                translationY = translateAnim
            }
    )
}

enum class StoneAnimationState {
    Invisible, Initial, Target
}