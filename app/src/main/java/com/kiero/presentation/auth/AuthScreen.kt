package com.kiero.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.data.auth.model.DummyEntity
import com.kiero.presentation.auth.component.DummyItem
import kotlinx.collections.immutable.PersistentList

@Composable
fun AuthRoute(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
    state: UiState<PersistentList<DummyEntity>>,
) {
    AuthScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        navigateUp = navigateUp,
        navigateToParent = navigateToParent,
        navigateToKid = navigateToKid,
        state = state,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun AuthScreen(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
    state: UiState<PersistentList<DummyEntity>>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is UiState.Loading -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Kiero App",
                            fontSize = 30.sp
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = navigateToParent,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "부모 화면으로 이동")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = navigateToKid,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "아이 화면으로 이동")
                        }
                    }
                }
            }

            is UiState.Empty -> {
                item {
                    Text(
                        modifier = modifier
                            .noRippleClickable { navigateUp() },
                        textAlign = TextAlign.Center,
                        text = "Dummy",
                        fontSize = 30.sp
                    )
                }
            }

            is UiState.Failure -> {
                item {
                    Text(
                        modifier = modifier
                            .noRippleClickable { navigateUp() },
                        textAlign = TextAlign.Center,
                        text = state.message,
                    )
                }
            }

            is UiState.Success -> {
                items(state.data) {
                    DummyItem(
                        id = it.id,
                        firstName = it.firstName,
                        lastName = it.lastName,
                        profileUrl = it.profile,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DummyScreenPreview() {
    KieroTheme {
        AuthScreen(
            paddingValues = PaddingValues(),
            snackbarHostState = SnackbarHostState(),
            navigateUp = {},
            navigateToParent = {},
            navigateToKid = {},
            state = UiState.Loading
        )
    }
}