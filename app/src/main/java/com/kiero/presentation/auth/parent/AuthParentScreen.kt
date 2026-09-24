package com.kiero.presentation.auth.parent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSingleEvent
import com.kiero.core.common.extension.toTrustedHttpsUrl
import com.kiero.core.designsystem.component.KieroToolTip
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.WebViewDialog
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.auth.parent.component.KakaoLoginButton
import com.kiero.presentation.auth.parent.component.TermsAgreementBottomSheet
import com.kiero.presentation.auth.parent.viewmodel.AuthParentViewModel
import com.kiero.presentation.auth.state.AuthSideEffect
import timber.log.Timber

@Composable
fun AuthParentRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
    navigateToParentSignUp: () -> Unit,
    navigateToParentGraph: () -> Unit,
    viewModel: AuthParentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val urlHandler = LocalUriHandler.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var webViewUrl by remember { mutableStateOf<String?>(null) }

    var isReviewerBypassDialogVisible by remember { mutableStateOf(false) }
    val reviewerLoginPassword = rememberTextFieldState()


    viewModel.sideEffect.collectSingleEvent {
        when (it) {
            is AuthSideEffect.NavigateUp -> navigateUp()
            is AuthSideEffect.NavigateToParentSignUp -> navigateToParentSignUp()

            is AuthSideEffect.ShowSnackbar -> {
                globalTrigger.showSnackbar(
                    SnackbarState(
                        message = it.message,
                        bottomPadding = 110
                    )
                )
            }

            is AuthSideEffect.OpenWebView -> {
                val safeUrl = it.url.toTrustedHttpsUrl(allowedHostSuffixes = NOTION_TERMS_HOST_SUFFIXES)
                if (safeUrl == null) {
                    globalTrigger.showToast("허용되지 않은 링크입니다.")
                    return@collectSingleEvent
                }
                try {
                    urlHandler.openUri(safeUrl)
                } catch (e: Exception) {
                    Timber.e(e)
                    webViewUrl = safeUrl
                }
            }

            AuthSideEffect.NavigateToParentGraph -> navigateToParentGraph()
            AuthSideEffect.NavigateToSelection -> navigateToSelection()
            else -> {}
        }
    }

    webViewUrl?.let { url ->
        WebViewDialog(url = url, onDismiss = { webViewUrl = null })
    }

    BackHandler {
        viewModel.navigateUp()
    }

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
            },
            onReviewerBypassButtonLongClick = {
                isReviewerBypassDialogVisible = true
            }
        )

        if (state.uiState is UiState.Loading) {
            KieroLoadingIndicator()
        }

        if (state.isShowTermsAgreement) {
            TermsAgreementBottomSheet(
                termsList = state.termsList,
                isAllAgreed = state.isAllAgreed,
                onDismiss = viewModel::showTermsAgreement,
                onClickTerms = viewModel::toggleTermsAccepted,
                navigateToTerms = viewModel::navigateToTerms,
                onConfirm = viewModel::successTermsAgreement
            )
        }

        if (isReviewerBypassDialogVisible) {
            ReviewerBypassDialog(
                passwordTextFieldState = reviewerLoginPassword,
                onDismiss = {
                    isReviewerBypassDialogVisible = false
                },
                onSubmit = viewModel::reviewerLogin
            )
        }
    }
}

private val NOTION_TERMS_HOST_SUFFIXES = setOf("notion.site")

@Composable
fun AuthParentScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    onLoginClick: () -> Unit,
    onReviewerBypassButtonLongClick: () -> Unit
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
                        .combinedClickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {},
                            onLongClick = onReviewerBypassButtonLongClick
                        )
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

@Composable
private fun ReviewerBypassDialog(
    passwordTextFieldState: TextFieldState,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reviewer Login") },
        text = {
            OutlinedTextField(
                state = passwordTextFieldState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
        },
        confirmButton = {
            TextButton(onClick = { onSubmit(passwordTextFieldState.text.toString()) }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Preview(showBackground = true, name = "기본 화면")
@Composable
private fun LoginScreenPreview() {
    KieroTheme {
        AuthParentScreen(
            paddingValues = PaddingValues(),
            onLoginClick = {},
            navigateUp = {},
            onReviewerBypassButtonLongClick = {}
        )
    }
}
