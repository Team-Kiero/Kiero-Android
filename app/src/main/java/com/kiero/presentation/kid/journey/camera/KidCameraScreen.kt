package com.kiero.presentation.kid.journey.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.journey.camera.component.StoneFloating
import com.kiero.presentation.kid.journey.camera.viewModel.KidCameraViewModel
import com.kiero.presentation.kid.journey.model.StoneUiType
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun KidCameraRoute(
    navigateUp: () -> Unit,
    viewModel: KidCameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val tempUri = remember {
        val directory = File(context.cacheDir, "images")
        directory.mkdirs()
        val file = File(directory, "captured_image.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    val systemCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = tempUri
        }
    }

    LaunchedEffect(Unit) {
        if (imageUri == null) {
            systemCameraLauncher.launch(tempUri)
        }
    }

    KidCameraScreen(
        imageUri = imageUri,
        stoneType = state.stoneType,
        onBackClick = navigateUp
    )
}

@Composable
private fun KidCameraScreen(
    imageUri: Uri?,
    stoneType: StoneUiType,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            delay(4000)
            onBackClick()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
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
                        append("우와! ")
                        withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                            append(stoneType.text)
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
                StoneFloating(stoneImageRes = stoneType.imageRes)

                Spacer(modifier = Modifier.height(19.dp))

                Image(
                    painter = painterResource(id = R.drawable.img_kid_camera_goblin),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 54.dp)
                        .offset(y = 32.dp)
                )
            }
        }
    }
}


@Composable
@Preview
private fun KidCameraScreenPreview() {
    KieroTheme {
        KidCameraScreen(
            imageUri = null,
            stoneType = StoneUiType.COURAGE,
            onBackClick = {}
        )
    }
}