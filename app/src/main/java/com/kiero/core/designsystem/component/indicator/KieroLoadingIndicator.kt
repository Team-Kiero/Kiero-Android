package com.kiero.core.designsystem.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.drawable.MovieDrawable
import com.kiero.R
import com.kiero.core.designsystem.component.animation.KieroAnimationType
import com.kiero.core.designsystem.component.animation.KieroAnimationView
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroLoadingIndicator(
    repeatCount : Int = MovieDrawable.REPEAT_INFINITE,
    onSuccess: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black),
        contentAlignment = Alignment.Center
    ) {
        KieroAnimationView(
            type = KieroAnimationType.Lottie(R.raw.kiero_skeleton),
            repeatCount = repeatCount,
            onSuccess = onSuccess
        )
    }
}

@Preview
@Composable
fun KieroLoadingIndicatorPreview() {
    KieroTheme {
        KieroLoadingIndicator()
    }
}