package com.kiero.presentation.parent.screen.journey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.journey.model.KidInfo

@Composable
fun ParentJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentJourneyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            ParentJourneySideEffect.NavigateUp -> navigateUp()
        }
    }

    ParentJourneyScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        state = state
    )
}

@Composable
private fun ParentJourneyScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    state: ParentJourneyState,
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = KieroTheme.colors.black
            )
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = KieroTheme.typography.bold.headLine2.toSpanStyle()
                            ) {
                                append(state.kidInfo.kidName)
                            }
                            append("의 오늘의 현황")
                        },
                        style = KieroTheme.typography.bold.headLine3,
                        color = KieroTheme.colors.white
                    )

                    Text(
                        text = state.currentDate,
                        style = KieroTheme.typography.regular.body4,
                        color = KieroTheme.colors.gray500
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(R.drawable.img_auth_kid_goblin_small),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(84f/96f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ParentJourneyScreenPreview() {
    KieroTheme {
        ParentJourneyScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = ParentJourneyState(
                kidInfo = KidInfo(
                    kidId = "1",
                    kidName = "손민성"
                ),
                currentDate = "2023-09-01"
            )
        )
    }
}
