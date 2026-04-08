package com.kiero.presentation.parent.screen.mission.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.animation.KieroAnimationType
import com.kiero.core.designsystem.component.animation.KieroAnimationView
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAutoLoadingScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KieroAnimationView(
                type = KieroAnimationType.Image(R.drawable.webp_parent),
                modifier = Modifier.padding(horizontal = 90.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "알림장을 분석하고 있어요!",
                style = KieroTheme.typography.semiBold.title2,
                color = KieroTheme.colors.white
            )
        }

    }
}

@Preview
@Composable
private fun ParentAutoLoadingScreenPreview() {
    KieroTheme {
        ParentAutoLoadingScreen(
            paddingValues = PaddingValues(),
        )
    }
}