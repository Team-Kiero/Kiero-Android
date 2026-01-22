package com.kiero.core.designsystem.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.drawable.MovieDrawable
import com.kiero.R
import com.kiero.core.designsystem.component.KieroGifImage
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
        KieroGifImage(
            drawableId = R.drawable.gif_loading,
            repeatCount = repeatCount,
            onSuccess = onSuccess
        )
    }
}