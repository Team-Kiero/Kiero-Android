package com.kiero.presentation.kid.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.mission.component.KidMissionItem
import com.kiero.presentation.kid.mission.component.KidMissionTitle
import com.kiero.presentation.kid.mission.state.KidMissionState

@Composable
fun KidMissionRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidMissionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    KidMissionScreen(
        paddingValues = paddingValues,
        state = state,
        navigateUp = navigateUp,
        onMissionCompleted = viewModel::onMissionCompleted
    )
}

// Todo 서버 값으로 변경
@Composable
private fun KidMissionScreen(
    paddingValues: PaddingValues,
    state: KidMissionState,
    navigateUp: () -> Unit,
    onMissionCompleted: (missionId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val painterGoblin = painterResource(id = R.drawable.img_kid_wish_goblin)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KidProfileChip(
                    kidName = state.kidName
                )

                Spacer(modifier = Modifier.weight(1f))

                KieroChip(
                    action = KieroCoinAction(
                        coinCount = 150,
                        isEnabled = true,
                        onClick = {}
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(5.dp))

            Image(
                painter = painterGoblin,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .forcePixelToDp(painterGoblin)
            )
        }

        // Todo 승재거 추가하기

        KidMissionState.FAKE.forEach { section ->
            item {
                KidMissionTitle(
                    title = section.headerTitle,
                    subTitle = section.subTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            items(
                items = section.missions,
                key = { it.id },
            ) { item ->
                KidMissionItem(
                    missionTitle = item.name,
                    missionReward = item.reward,
                    isCompleted = item.isCompleted,
                    onClickButton = {
                        onMissionCompleted(item.id)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun KidWishScreenPreview() {
    KieroTheme {
        KidMissionScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            onMissionCompleted = {},
            state = KidMissionState()
        )
    }
}