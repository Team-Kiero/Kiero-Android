package com.kiero.presentation.main.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.designsystem.component.KieroSnackbar
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.model.trigger.DialogTrigger
import com.kiero.core.model.trigger.GlobalUiEventHolder
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.main.navigation.KidMainTab
import com.kiero.presentation.main.navigation.KieroNavHost
import com.kiero.presentation.main.navigation.MainAppState
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.main.navigation.component.BottomBarTab
import com.kiero.presentation.main.navigation.component.MainBottomBar
import com.kiero.presentation.main.state.rememberDialogStateHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainRoute(
    appState: MainAppState,
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    val showParentBottomBar by appState.showParentBottomBar.collectAsStateWithLifecycle()
    val showKidBottomBar by appState.showKidBottomBar.collectAsStateWithLifecycle()

    val currentParentTab by appState.currentParentTab.collectAsStateWithLifecycle()
    val currentKidTab by appState.currentKidTab.collectAsStateWithLifecycle()
    var currentSnackbarState by remember { mutableStateOf<SnackbarState?>(null) }

    val isVisible = showParentBottomBar || showKidBottomBar

    var cachedTabs by remember {
        mutableStateOf(ParentMainTab.entries.toImmutableList() as ImmutableList<BottomBarTab>)
    }

    var cachedShape by remember {
        mutableStateOf<Shape>(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
    }

    if (showParentBottomBar) {
        cachedTabs = ParentMainTab.entries.toImmutableList()
        cachedShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
    } else if (showKidBottomBar) {
        cachedTabs = KidMainTab.entries.toImmutableList()
        cachedShape = RoundedCornerShape(0.dp)
    }

    val tabs = cachedTabs
    val containerShape = cachedShape

    val currentTab =
        if (showParentBottomBar) currentParentTab else if (showKidBottomBar) currentKidTab else null
    val dialogState = rememberDialogStateHolder()

    val onShowToast: (String) -> Unit = remember {
        { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val onShowSnackbar: (SnackbarState) -> Unit = remember(scope, snackBarHostState) {
        { state ->
            currentSnackbarState = state
            scope.launch {
                snackBarHostState.currentSnackbarData?.dismiss()

                val job = launch {
                    snackBarHostState.showSnackbar(
                        message = state.message,
                    )
                }
                job.invokeOnCompletion {
                    if (currentSnackbarState == state) {
                        currentSnackbarState = null
                    }
                }
                delay(2000L)
                job.cancel()
            }
        }
    }

    val eventHolder = remember(dialogState, onShowToast, onShowSnackbar) {
        GlobalUiEventHolder(
            dialogTrigger = DialogTrigger(
                show = { onConfirm ->
                    dialogState.showDialog(onConfirm)
                },
                dismiss = {
                    dialogState.dismissDialog()
                }
            ),
            showToast = onShowToast,
            showSnackbar = onShowSnackbar
        )
    }

    LaunchedEffect(isOffline) {
        Timber.e("네트워크 상태 변경 감지: isOffline = $isOffline")

        if (isOffline) {
            if (!dialogState.dialogState.isVisible) {
                eventHolder.dialogTrigger.show {}
            }
        } else {
            if (dialogState.dialogState.isVisible) {
                dialogState.dismissDialog()
            }
        }
    }

    HandleBackPressToExit(
        onShowToast = {
            onShowToast("버튼을 한번 더 누르면 앱이 종료됩니다.")
        }
    )

    CompositionLocalProvider(
        LocalGlobalUiEventTrigger provides eventHolder
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState) { data ->
                        KieroSnackbar(
                            message = data.visuals.message,
                            // TODO: 디자인 확정 후 스낵바 높이 및 패딩 수정 필요
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 90.dp)
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
                if (dialogState.dialogState.isVisible) {
                    KieroDialog(
                        title = "인터넷 연결 확인해주세요!",
                        confirmAction = KieroConfirmAction(
                            text = "확인",
                            onClick = {
                                if (!isOffline) {
                                    dialogState.dismissDialog()
                                }
                            }
                        ),
                        onDismiss = {
                            if (!isOffline) {
                                dialogState.dismissDialog()
                            }
                        }
                    )
                }

                KieroNavHost(
                    appState = appState,
                    paddingValues = paddingValues,
                    startDestination = appState.startDestination
                )
            }
        }
    }
}

@Composable
private fun HandleBackPressToExit(
    enabled: Boolean = true,
    exitDuration: Long = 2000L,
    onShowToast: () -> Unit = {}
) {
    val activity = LocalActivity.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        if (System.currentTimeMillis() - backPressedTime <= exitDuration) {
            activity?.finish()
        } else {
            onShowToast()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
