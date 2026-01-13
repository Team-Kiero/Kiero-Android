package com.kiero.presentation.auth.parent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.component.KieroToolTip
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.auth.model.AuthSideEffect
import com.kiero.presentation.auth.parent.component.KakaoLoginButton
import com.kiero.presentation.auth.parent.viewmodel.AuthParentViewModel

@Composable
fun AuthParentRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: AuthParentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is AuthSideEffect.NavigateUp -> navigateUp()
        }
    }

    AuthParentScreen(
        paddingValues = paddingValues,
        isLoading = state.isLoading,
        navigateUp = viewModel::navigateUp,
        onLoginClick = { viewModel.loginWithKakao(context) }
    )
}

@Composable
fun AuthParentScreen(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    navigateUp: () -> Unit,
    onLoginClick: () -> Unit,
) {
    val painter = painterResource(id = R.drawable.img_auth_parent_goblin)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                modifier = Modifier
                    .forcePixelToDp(painter)
            )

            KieroToolTip(
                message = "반가워요!",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-200).dp, y = 210.dp)
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

@Preview(showBackground = true, name = "기본 화면")
@Composable
private fun LoginScreenPreview() {
    KieroTheme {
        AuthParentScreen(
            paddingValues = PaddingValues(),
            isLoading = false,
            onLoginClick = {},
            navigateUp = {}
        )
    }
}