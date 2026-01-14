package com.kiero.presentation.auth.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.auth.model.AuthSideEffect
import com.kiero.presentation.auth.parent.component.KakaoLoginButton
import com.kiero.presentation.auth.parent.viewmodel.ParentLoginViewModel

@Composable
fun AuthParentRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit, // ✅ 추가: 부모 메인으로 이동하는 콜백
    viewModel: ParentLoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AuthSideEffect.ShowSnackBar -> { /* 스낵바 로직 */
                }

                is AuthSideEffect.NavigateUp -> {
                    // Todo : 신규 자녀 추가 화면 이동
                    navigateToParent()
                    // 로그인이 성공했을 때 메인으로 이동하도록 변경
                }
            }
        }
    }
    AuthParentScreen(
        paddingValues = paddingValues,
        isLoading = state.isLoading,
        onLoginClick = { viewModel.loginWithKakao(context) }
    )
}

@Composable
fun AuthParentScreen(
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
        AuthParentScreen(
            paddingValues = PaddingValues(),
            isLoading = false,
            onLoginClick = {}
        )
    }
}