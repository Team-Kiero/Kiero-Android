package com.kiero.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun KieroGifImage(
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageRequest = remember(drawableId) {
        ImageRequest.Builder(context)
            .data(drawableId)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}