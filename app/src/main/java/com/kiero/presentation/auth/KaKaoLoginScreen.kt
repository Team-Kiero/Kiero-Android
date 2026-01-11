package com.kiero.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.auth.component.KakaoLoginButton
import com.kiero.presentation.auth.model.AuthContract
import com.kiero.presentation.auth.viewmodel.AuthViewModel

@Composable
fun KaKaoLoginRoute(
    paddingValues: PaddingValues,
    onLoginSuccess: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AuthContract.SideEffect.LoginSuccess -> onLoginSuccess()
                is AuthContract.SideEffect.ShowSnackBar -> { /* 스낵바 로직 */ }
                is AuthContract.SideEffect.NavigateUp -> navigateUp()
            }
        }
    }

    KakaoLoginScreen(
        paddingValues = paddingValues,
        isLoading = state.isLoading,
        onLoginClick = viewModel::loginWithKakao
    )
}

@Composable
fun KakaoLoginScreen(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단에 빈 공간을 채워 버튼을 아래로 밀어내기
        Spacer(modifier = Modifier.weight(1f))

        KakaoLoginButton(
            onClick = onLoginClick,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(65.dp))
    }
}

@Preview(showBackground = true, name = "기본 화면")
@Composable
private fun LoginScreenPreview() {
    KieroTheme {
        KakaoLoginScreen(
            paddingValues = PaddingValues(),
            isLoading = false,
            onLoginClick = {}
        )
    }
}