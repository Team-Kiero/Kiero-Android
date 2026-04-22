package com.kiero.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSingleEvent
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.auth.UserRole
import com.kiero.presentation.auth.component.AuthButton
import com.kiero.presentation.auth.state.AuthSideEffect
import com.kiero.presentation.auth.state.AuthState
import com.kiero.presentation.auth.viewmodel.AuthViewModel

@Composable
fun AuthRoute(
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSingleEvent {
        when (it) {
            AuthSideEffect.NavigateToParent -> navigateToParent()
            AuthSideEffect.NavigateToKid -> navigateToKid()
            else -> {}
        }
    }

    AuthScreen(
        state = state,
        onClickRole = viewModel::fetchRole,
        onClickAuth = viewModel::startRole
    )
}

@Composable
fun AuthScreen(
    state: AuthState,
    onClickAuth: () -> Unit,
    onClickRole: (UserRole) -> Unit,
    modifier: Modifier = Modifier,
) {
    val logoPainter = painterResource(id = R.drawable.ic_logo)

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.img_splash_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x00232428),
                                Color(0xFF232428)
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(186f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "아이의 하루가 모험이 되는 곳",
                    style = KieroTheme.typography.regular.body1,
                    color = KieroTheme.colors.schedule1,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 70.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    painter = logoPainter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(250f))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "어떤 사용자가 사용하시나요?",
                    style = KieroTheme.typography.semiBold.title3,
                    color = KieroTheme.colors.gray200,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(17.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AuthButton(
                        text = "부모님으로",
                        modifier = Modifier.weight(1f),
                        selectedRole = state.userRole,
                        buttonRole = UserRole.PARENT,
                        onClickButton = {
                            onClickRole(UserRole.PARENT)
                        }
                    )

                    AuthButton(
                        text = "자녀로",
                        modifier = Modifier.weight(1f),
                        selectedRole = state.userRole,
                        buttonRole = UserRole.KID,
                        onClickButton = {
                            onClickRole(UserRole.KID)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            KieroButtonMedium(
                text = "시작하기",
                isEnabled = state.userRole != null,
                onClick = onClickAuth,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(41f))
        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    KieroTheme {
        AuthScreen(
            state = AuthState(),
            onClickRole = {},
            onClickAuth = {}
        )
    }
}
