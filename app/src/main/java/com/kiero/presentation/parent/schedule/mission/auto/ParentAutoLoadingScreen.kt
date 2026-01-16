package com.kiero.presentation.parent.schedule.mission.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel

// Todo : 네비게이션 연결
@Composable
fun ParentAutoLoadingRoute(
    paddingValues: PaddingValues,
    viewModel: AutoMissionViewModel = hiltViewModel()
) {
    val globalUiEvent = LocalGlobalUiEventTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is AutoMissionSideEffect.ShowToast -> {
                    globalUiEvent.showToast(sideEffect.message)
                }
                else -> {}
            }
        }
    }

    ParentAutoLoadingScreen(
        paddingValues = paddingValues,
    )
}

@Composable
fun ParentAutoLoadingScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = KieroTheme.colors.black)
            .offset(y = (-40).dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Todo : Box -> KieroGifImage 대체
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = KieroTheme.colors.gray800,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "알림장을 분석하고 있어요!",
            style = KieroTheme.typography.semiBold.title2,
            color = KieroTheme.colors.white
        )
    }
}

@Preview
@Composable
private fun ParentAutoLoadingScreenPreview_NoSnackbar() {
    KieroTheme {
        ParentAutoLoadingScreen(
            paddingValues = PaddingValues(),
        )
    }
}

@Preview
@Composable
private fun ParentAutoLoadingScreenPreview_WithSnackbar() {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        snackbarHostState.showSnackbar("테스트용 스낵바 메시지입니다.")
    }
    KieroTheme {
        ParentAutoLoadingScreen(
            paddingValues = PaddingValues(),
        )
    }
}
