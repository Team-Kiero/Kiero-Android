package com.kiero.presentation.kid.journey.fire

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.component.KieroToolTip
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroTextAction
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.journey.fire.component.KieroButtonLarge
import com.kiero.presentation.kid.journey.fire.state.KidFireState
import com.kiero.presentation.kid.journey.fire.viewModel.KidFireViewModel

@Composable
fun KidFireRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToFireResult: (String) -> Unit,
    viewModel: KidFireViewModel = hiltViewModel()
) {
    val state by viewModel.fireState.collectAsStateWithLifecycle()

    when (val state = state) {
        is UiState.Success -> {
            KidFIreScreen(
                paddingValues = paddingValues,
                state = state.data,
                navigateUp = navigateUp,
                onClick = { navigateToFireResult(state.data.date) }
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
private fun KidFIreScreen(
    paddingValues: PaddingValues,
    state: KidFireState,
    navigateUp: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KieroTopbar(
                title = state.date,
                leftIconRes = R.drawable.ic_arrow_left,
                leftIconClick = navigateUp,
                modifier = Modifier
                    .padding(top = 20.dp),
                textColor = KieroTheme.colors.gray500,
                textStyle = KieroTheme.typography.regular.body3
            )

            // 배경 이미지
            Image(
                painter = painterResource(id = R.drawable.img_kid_journey_mask_background),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 86.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .forcePixelToDp(painterResource(id = R.drawable.img_kid_journey_mask_background))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KieroToolTip(
                message = "불조각을 나에게 건네줘!",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 156.dp)
            )

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_kid_goblin),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 44.dp)
                )

                KieroChip(
                    action = KieroTextAction(
                        text = "꾸비",
                        onClick = {}
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 1.dp),
                    horizontalPadding = 20,
                    verticalPadding = 4,
                )
            }
            KieroButtonLarge(
                stoneCount = state.stones,
                onClick = onClick,
                modifier = Modifier
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
@Preview
private fun KidFIreScreenPreview() {
    KieroTheme {
        KidFIreScreen(
            paddingValues = PaddingValues(),
            state = KidFireState(),
            navigateUp = {},
            onClick = {}
        )
    }
}