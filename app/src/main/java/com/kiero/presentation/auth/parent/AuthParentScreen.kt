package com.kiero.presentation.auth.parent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSingleEvent
import com.kiero.core.designsystem.component.KieroToolTip
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.auth.parent.component.KakaoLoginButton
import com.kiero.presentation.auth.parent.viewmodel.AuthParentViewModel
import com.kiero.presentation.auth.state.AuthSideEffect

@Composable
fun AuthParentRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToParentSignUp: (String, String) -> Unit,
    navigateToParentGraph: () -> Unit,
    viewModel: AuthParentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSingleEvent {
        when (it) {
            is AuthSideEffect.NavigateUp -> navigateUp()
            is AuthSideEffect.NavigateToParentSignUp -> {
                navigateToParentSignUp(
                    it.parentName, it.parentProfileImage
                )
            }

            is AuthSideEffect.ShowSnackbar -> {
                globalTrigger.showSnackbar(
                    SnackbarState(
                        message = it.message
                    )
                )
            }

            AuthSideEffect.NavigateToParentGraph -> navigateToParentGraph()

            else -> {}
        }
    }

    BackHandler(
        enabled = true,
        onBack = viewModel::navigateUp
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AuthParentScreen(
            paddingValues = paddingValues,
            navigateUp = viewModel::navigateUp,
            onLoginClick = {
                if (state.uiState !is UiState.Loading) {
                    viewModel.loginWithKakao(context)
                }
            }
        )

        if (state.uiState is UiState.Loading) {
            KieroLoadingIndicator()
        }
    }
}

@Composable
fun AuthParentScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    onLoginClick: () -> Unit,
) {
    val painter = painterResource(id = R.drawable.img_auth_parent_goblin)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KieroTheme.colors.black)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            KieroTopbar(
                title = "부모님으로 시작하기",
                leftIconRes = R.drawable.ic_arrow_left,
                leftIconClick = navigateUp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize()
                )

                KieroToolTip(
                    message = "반가워요!",
                    modifier = Modifier
                        .align(BiasAlignment(
                            horizontalBias = -0.4f,
                            verticalBias = -0.5f
                        ))
                )

                KakaoLoginButton(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true, name = "기본 화면")
@Composable
private fun LoginScreenPreview() {
    KieroTheme {
        AuthParentScreen(
            paddingValues = PaddingValues(),
            onLoginClick = {},
            navigateUp = {}
        )
    }
}