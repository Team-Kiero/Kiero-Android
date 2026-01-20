package com.kiero.core.designsystem.component

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.drawable.MovieDrawable
import coil.request.ImageRequest
import coil.request.onAnimationEnd
import coil.request.repeatCount

@Composable
fun KieroGifImage(
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
    repeatCount : Int = MovieDrawable.REPEAT_INFINITE,
    onSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val imageRequest = remember(drawableId) {
        ImageRequest.Builder(context)
            .data(drawableId)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .allowHardware(false)
            .repeatCount(repeatCount)
            .onAnimationEnd {
                onSuccess()
            }
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}