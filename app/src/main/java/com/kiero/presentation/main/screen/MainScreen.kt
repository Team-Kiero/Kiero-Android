package com.kiero.presentation.main.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.designsystem.component.KieroSnackbar
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.DialogTrigger
import com.kiero.core.model.trigger.GlobalUiEventHolder
import com.kiero.core.model.trigger.RefreshState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.navigation.Route
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.main.component.ParentTopbar
import com.kiero.presentation.main.navigation.KidMainTab
import com.kiero.presentation.main.navigation.KieroNavHost
import com.kiero.presentation.main.navigation.MainAppState
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.main.navigation.component.BottomBarTab
import com.kiero.presentation.main.navigation.component.MainBottomBar
import com.kiero.presentation.main.state.MainState
import com.kiero.presentation.main.state.rememberDialogStateHolder
import com.kiero.presentation.main.viewmodel.MainViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainRoute(
    appState: MainAppState,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }

    MainScreen(
        appState = appState,
        state = state,
        snackBarHostState = snackBarHostState,
        onAlarmClick = viewModel::onAlarmRead,
        onRefreshUnreadAlarm = viewModel::fetchUnreadAlarmStatus
    )
}

@Composable
fun MainScreen(
    appState: MainAppState,
    state: MainState,
    snackBarHostState: SnackbarHostState,
    onAlarmClick: () -> Unit,
    onRefreshUnreadAlarm: () -> Unit,
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

    var bottomPadding by remember {
        mutableIntStateOf(90)
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
    val refreshState = remember { RefreshState() }

    val onShowToast: (String) -> Unit = remember {
        { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val onShowSnackbar: (SnackbarState) -> Unit = remember(scope, snackBarHostState) {
        { state ->
            currentSnackbarState = state
            bottomPadding = state.bottomPadding
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

    val tabReselectedEvent = remember { MutableSharedFlow<Route>() }
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
            showSnackbar = onShowSnackbar,
            onTabReselected = { route ->
                scope.launch {
                    tabReselectedEvent.emit(route)
                }
            }
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
        LocalGlobalUiEventTrigger provides eventHolder,
        LocalRefreshState provides refreshState
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(KieroTheme.colors.black)
                .navigationBarsPadding()
        ) {
            if (currentTab == ParentMainTab.JOURNEY) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .background(KieroTheme.colors.gray900)
                )
            }

            Scaffold(
                containerColor = KieroTheme.colors.black,
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState) { data ->
                        KieroSnackbar(
                            message = data.visuals.message,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = bottomPadding.dp)
                        )
                    }
                },
                topBar = {
                    when (showParentBottomBar) {
                        true -> {
                            ParentTopbar(
                                title = when (currentTab) {
                                    ParentMainTab.JOURNEY -> ""
                                    null -> ""
                                    else -> stringResource(currentTab.contentDescription)
                                },
                                backgroundColor = when (currentTab) {
                                    ParentMainTab.JOURNEY -> KieroTheme.colors.gray900
                                    else -> KieroTheme.colors.black
                                },
                                isAlarmActive = state.unreadAlarm.hasUnread,
                                onAlarmClick = {
                                    onAlarmClick()
                                    appState.navigateToAlarm()
                                }
                            )
                        }

                        false -> {

                        }
                    }
                },
                bottomBar = {
                    MainBottomBar(
                        isVisible = isVisible,
                        isParentMode = showParentBottomBar,
                        containerShape = containerShape,
                        tabs = tabs,
                        currentTab = currentTab,
                        onTabSelected = { tab ->
                            if (currentTab == tab) {
                                val route = when (tab) {
                                    is ParentMainTab -> tab.route
                                    is KidMainTab -> tab.route
                                    else -> null
                                }
                                route?.let { eventHolder.onTabReselected(it) }
                                scope.launch {
                                    refreshState.trigger(tab)
                                }
                            } else {
                                when (tab) {
                                    is ParentMainTab -> appState.navigateParentTab(tab)
                                    is KidMainTab -> appState.navigateKidTab(tab)
                                }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                if (dialogState.dialogState.isVisible) {
                    KieroDialog(
                        title = "인터넷 연결을 확인해주세요!",
                        isDisabled = true,
                        confirmAction = KieroConfirmAction(
                            text = "재시도",
                            onClick = {
                                if (!isOffline) {
                                    dialogState.dismissDialog()
                                } else {
                                    onShowToast("네트워크가 아직 연결되지 않았습니다.")
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
                    startDestination = appState.startDestination,
                    onRefreshUnreadAlarm = onRefreshUnreadAlarm
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
