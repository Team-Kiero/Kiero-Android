package com.kiero.presentation.main.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.designsystem.component.KieroSnackbar
import com.kiero.presentation.main.navigation.KidMainTab
import com.kiero.presentation.main.navigation.KieroNavHost
import com.kiero.presentation.main.navigation.MainAppState
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.main.navigation.component.MainBottomBar
import com.kiero.presentation.main.navigation.rememberMainAppState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainRoute(
    appState: MainAppState = rememberMainAppState()
) {
    val snackBarHostState = remember { SnackbarHostState() }

    MainScreen(
        appState = appState,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
fun MainScreen(
    appState: MainAppState,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val showParentBottomBar by appState.showParentBottomBar.collectAsStateWithLifecycle()
    val showKidBottomBar by appState.showKidBottomBar.collectAsStateWithLifecycle()
    val currentParentTab by appState.currentParentTab.collectAsStateWithLifecycle()
    val currentKidTab by appState.currentKidTab.collectAsStateWithLifecycle()

    val isVisible = showParentBottomBar || showKidBottomBar
    val containerShape = if (showParentBottomBar) {
        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
    } else {
        RoundedCornerShape(0.dp)
    }
    val tabs = if (showParentBottomBar) {
        ParentMainTab.entries.toImmutableList()
    } else {
        KidMainTab.entries.toImmutableList()
    }
    val currentTab = if (showParentBottomBar) currentParentTab else currentKidTab

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                KieroSnackbar(
                    message = data.visuals.message,
                    // TODO: 디자인 확정 후 스낵바 높이 및 패딩 수정 필요
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        bottomBar = {
            MainBottomBar(
                isVisible = isVisible,
                containerShape = containerShape,
                tabs = tabs,
                currentTab = currentTab,
                onTabSelected = { tab ->
                    when (tab) {
                        is ParentMainTab -> appState.navigateParentTab(tab)
                        is KidMainTab -> appState.navigateKidTab(tab)
                    }
                }
            )
        }
    ) { paddingValues ->
        KieroNavHost(
            appState = appState,
            paddingValues = paddingValues,
        )
    }
}