package com.kiero.presentation.kid.journey.camera

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidSpeechField

@Composable
fun KidCameraRoute(
    navigateUp: () -> Unit,
) {
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val systemCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            capturedBitmap = bitmap
        } else if (capturedBitmap == null) {
            navigateUp()
        }
    }

    LaunchedEffect(Unit) {
        if (capturedBitmap == null) {
            systemCameraLauncher.launch()
        }
    }

    KidCameraScreen(
        bitmap = capturedBitmap,
        onBackClick = navigateUp
    )
}

@Composable
private fun KidCameraScreen(
    bitmap: Bitmap?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawRect(color = Color.Black.copy(alpha = 0.4f))
                    },
                contentScale = ContentScale.Crop
            )

            KidSpeechField(
                modifier = modifier
                    .padding(top = 87.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("우와!")
                        withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                            append("용기의 불조각")
                        }
                        append(" 을 손에 넣었어!")
                    },
                    color = KieroTheme.colors.gray300
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StoneFloating()

                Spacer(modifier = Modifier.height(19.dp))

                Image(
                    painter = painterResource(id = R.drawable.img_kid_camera_goblin),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 54.dp)
                )
            }
        }
    }
}

@Composable
fun StoneFloating(
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
        painter = painterResource(id = R.drawable.img_kid_journey_stone_blue),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 110.dp)
            .graphicsLayer {
                this.translationY = floatAnim
            }
    )
}

@Composable
@Preview
private fun KidCameraScreenPreview() {
    KieroTheme {
        val context = LocalContext.current
        val drawable = androidx.core.content.ContextCompat.getDrawable(
            context,
            android.R.drawable.ic_menu_gallery
        )
        val dummyBitmap = remember {
            Bitmap.createBitmap(
                100, 100, Bitmap.Config.ARGB_8888
            )
        }

        KidCameraScreen(
            // null 대신 더미 비트맵 전달
            bitmap = dummyBitmap,
            onBackClick = {}
        )
    }
}