package com.kiero.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.common.extension.collectSingleEvent
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.auth.UserRole
import com.kiero.presentation.auth.component.AuthButton
import com.kiero.presentation.auth.state.AuthSideEffect
import com.kiero.presentation.auth.viewmodel.AuthViewModel

@Composable
fun AuthRoute(
    paddingValues: PaddingValues,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    viewModel.sideEffect.collectSingleEvent {
        when (it) {
            AuthSideEffect.NavigateToParent -> navigateToParent()
            AuthSideEffect.NavigateToKid -> navigateToKid()
            else -> {}
        }
    }

    AuthScreen(
        paddingValues = paddingValues,
        onClickAuth = viewModel::fetchRole
    )
}

@Composable
fun AuthScreen(
    paddingValues: PaddingValues,
    onClickAuth: (UserRole) -> Unit,
    modifier: Modifier = Modifier,
) {
    val logoPainter = painterResource(id = R.drawable.img_auth_app_logo)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = logoPainter,
            contentDescription = null,
            modifier = Modifier.forcePixelToDp(logoPainter)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "아이의 하루가 모험이 되는 곳",
            style = KieroTheme.typography.regular.body1,
            color = KieroTheme.colors.schedule1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            AuthButton(
                icon = R.drawable.img_auth_parent_goblin_small,
                text = "부모님으로 시작하기",
                onClickButton = {
                    onClickAuth(UserRole.PARENT)
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            AuthButton(
                icon = R.drawable.img_auth_kid_goblin_small,
                text = "자녀로 시작하기",
                onClickButton = {
                    onClickAuth(UserRole.KID)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun DummyScreenPreview() {
    KieroTheme {
        AuthScreen(
            paddingValues = PaddingValues(),
            onClickAuth = {}
        )
    }
}