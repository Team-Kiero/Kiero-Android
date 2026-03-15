package com.kiero.presentation.kid.journey.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.journey.map.component.KidMapEmptyView
import com.kiero.presentation.kid.journey.map.component.KidMapHeader
import com.kiero.presentation.kid.journey.map.component.KidMapScheduleList
import com.kiero.presentation.kid.journey.map.state.KidMapState
import com.kiero.presentation.kid.journey.map.viewModel.KidMapViewModel

@Composable
fun KidMapRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidMapViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val state = state) {
        is UiState.Loading -> KieroLoadingIndicator()
        is UiState.Success -> KidMapScreen(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            state = state.data
        )
        is UiState.Failure -> Unit
        is UiState.Empty -> Unit
    }
}

@Composable
private fun KidMapScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    state: KidMapState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_kid_map_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            KieroTopbar(
                title = state.date,
                leftIconRes = R.drawable.ic_arrow_left,
                leftIconClick = navigateUp,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                if (state.schedules.isNotEmpty()) {
                    KidMapHeader(scheduleCount = state.scheduleCount)

                    Spacer(modifier = Modifier.height(25.dp))

                    KidMapScheduleList(
                        schedules = state.schedules,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    KidMapEmptyView(
                        modifier = Modifier.weight(1f)
                    )
                }

                KieroButtonMedium(
                    text = "확인",
                    onClick = navigateUp,
                    containerColor = KieroTheme.colors.gray100,
                    contentColor = KieroTheme.colors.gray900,
                    modifier = Modifier
                        .padding(bottom = 57.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun KidMapScreenPreview() {
    KieroTheme {
        KidMapScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = KidMapState.FAKE
        )
    }
}