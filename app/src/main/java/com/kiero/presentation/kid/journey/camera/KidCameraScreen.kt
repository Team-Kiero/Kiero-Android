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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.util.successData
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.journey.camera.component.KidCameraStoneFloating
import com.kiero.presentation.kid.journey.camera.state.KidCameraSideEffect
import com.kiero.presentation.kid.journey.camera.viewModel.KidCameraViewModel
import com.kiero.presentation.kid.journey.model.StoneUiType

@Composable
fun KidCameraRoute(
    navigateUp: () -> Unit,
    viewModel: KidCameraViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.createTempFile()
    }

    val systemCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.updateImageUri()

            viewModel.postImage(
                fileName = "${System.currentTimeMillis()}.jpg",
                contentType = "image/jpeg"
            )
        }
    }

    viewModel.sideEffect.collectSideEffect {
        when(it) {
            is KidCameraSideEffect.NavigateUp -> {
                navigateUp()
            }
        }
    }

    LaunchedEffect(state.successData?.tempUri) {
        if (state.successData?.tempUri != null && state.successData?.imageUri == null) {
            systemCameraLauncher.launch(state.successData?.tempUri!!.toUri())
        }
    }

    when (val state = state) {
        is UiState.Success -> {
            KidCameraScreen(
                imageUri = state.successData?.imageUri?.toUri(),
                stoneType = state.successData?.stoneType!!
            )
        }
        is UiState.Loading -> {
            KieroLoadingIndicator()
        }
        is UiState.Failure -> {}
        else -> {}
    }
}

@Composable
private fun KidCameraScreen(
    imageUri: Uri?,
    stoneType: StoneUiType,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
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
                modifier = Modifier
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
                KidCameraStoneFloating(
                    stoneImageRes = stoneType.imageRes,
                    modifier = Modifier.padding(horizontal = 110.dp)
                )

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
            stoneType = StoneUiType.COURAGE
        )
    }
}