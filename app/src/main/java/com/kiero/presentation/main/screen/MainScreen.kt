package com.kiero.presentation.main.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.presentation.main.navigation.KidMainTab
import com.kiero.presentation.main.navigation.KieroNavHost
import com.kiero.presentation.main.navigation.MainNavigator
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.main.navigation.component.MainBottomBar
import com.kiero.presentation.main.navigation.rememberMainNavigator
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainRoute(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    MainScreen(
        navigator = navigator,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
private fun MainScreen(
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            MainBottomBar(
                isVisible = navigator.showParentBottomBar(),
                tabs = ParentMainTab.entries.toImmutableList(),
                currentTab = navigator.currentParentTab,
                onTabSelected = { navigator.navigateParentTab(it) },
                containerShape = RoundedCornerShape(
                    topStart = 15.dp,
                    topEnd = 15.dp
                ),
            )

            MainBottomBar(
                isVisible = navigator.showKidBottomBar(),
                tabs = KidMainTab.entries.toImmutableList(),
                currentTab = navigator.currentKidTab,
                onTabSelected = { navigator.navigateKidTab(it) },
                containerShape = RoundedCornerShape(0.dp),
            )
        }
    ) { paddingValues ->
        KieroNavHost(
            navigator = navigator,
            paddingValues = paddingValues,
            snackbarHostState = snackBarHostState,
        )
    }
}