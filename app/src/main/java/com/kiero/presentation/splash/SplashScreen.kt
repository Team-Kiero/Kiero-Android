package com.kiero.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.common.extension.collectSingleEvent
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.splash.component.SplashLightFloating
import com.kiero.presentation.splash.state.SplashSideEffect
import com.kiero.presentation.splash.viewmodel.SplashViewModel


@Composable
fun SplashRoute(
    navigateToAuth: () -> Unit,
    navigateToParentHome: () -> Unit,
    navigateToParentGraph: () -> Unit,
    navigateToKidHome: () -> Unit,
    navigateToKidOnboarding: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    viewModel.sideEffect.collectSingleEvent {
        when (it) {
            SplashSideEffect.NavigateToAuth -> navigateToAuth()
            SplashSideEffect.NavigateToKidHome -> navigateToKidHome()
            SplashSideEffect.NavigateToParentHome -> navigateToParentHome()
            SplashSideEffect.NavigateToParentGraph -> navigateToParentGraph()
            SplashSideEffect.NavigateToKidOnboarding -> navigateToKidOnboarding()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkLoginState()
    }

    SplashScreen()
}

@Composable
fun SplashScreen() {
    val logoPainter = painterResource(R.drawable.ic_logo)
    val goblinPainter = painterResource(R.drawable.img_splash_goblin)
    val configuration = LocalConfiguration.current.screenWidthDp.dp


    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.img_splash_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )

        SplashLightFloating(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = configuration * 0.1f, y = -(configuration * 1f))
        )

        SplashLightFloating(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = -(configuration * 0.05f), y = configuration * 0.3f)
        )

        SplashLightFloating(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = configuration * 0.03f, y = -(configuration * 0.5f))
        )

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "아이의 하루가 모험이 되는 곳",
                style = KieroTheme.typography.regular.body2,
                color = KieroTheme.colors.schedule1,
                textAlign = TextAlign.Start,

            )

            Image(
                painter = logoPainter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(width = 300.dp, height = 63.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = goblinPainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(width = configuration * 2f, height = configuration * 1.2f),
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    KieroTheme {
        SplashScreen()
    }
}