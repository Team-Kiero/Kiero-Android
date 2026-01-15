package com.kiero.presentation.parent.schedule.mission.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.designsystem.component.KieroSnackbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel

// Todo : 네비게이션 연결
@Composable
fun ParentAutoLoadingRoute(
    paddingValues: PaddingValues,
    viewModel: AutoMissionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is AutoMissionSideEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(sideEffect.message)
                }
                else -> {}
            }
        }
    }

    ParentAutoLoadingScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun ParentAutoLoadingScreen(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 60.dp)
            ) { snackbarData ->
                KieroSnackbar(
                    message = snackbarData.visuals.message,
                    modifier = Modifier.padding(horizontal = 20.dp)
                    // TODO: KieroSnackbar 색상 변경 (현재 공통 컴포넌트 색상 고정 상태)
                )
            }
        },
        containerColor = KieroTheme.colors.black
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(innerPadding)
                .background(color = KieroTheme.colors.black)
                .offset(y = (-40).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Todo : Box -> KieroGifImage 대체

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(color = KieroTheme.colors.gray800, shape = CircleShape),
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
}

@Preview
@Composable
private fun ParentAutoLoadingScreenPreview_NoSnackbar() {
    KieroTheme {
        ParentAutoLoadingScreen(
            paddingValues = PaddingValues(),
            snackbarHostState = remember { SnackbarHostState() }
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
            snackbarHostState = snackbarHostState
        )
    }
}
