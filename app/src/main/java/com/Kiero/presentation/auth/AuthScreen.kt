package com.Kiero.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.Kiero.core.common.extension.noRippleClickable
import com.Kiero.core.model.UiState
import com.Kiero.core.designsystem.theme.KieroTheme
import com.Kiero.domain.auth.model.DummyEntity
import com.Kiero.presentation.auth.component.DummyItem
import kotlinx.collections.immutable.PersistentList

@Composable
fun AuthRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateNext: () -> Unit,
    state: UiState<PersistentList<DummyEntity>>,
) {
    AuthScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        navigateNext = navigateNext,
        state = state,
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun AuthScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateNext: () -> Unit,
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
                    Text(
                        modifier = modifier
                            .noRippleClickable { navigateUp() },
                        textAlign = TextAlign.Center,
                        text = "Dummy",
                        fontSize = 30.sp
                    )
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
                        navigateNext = navigateNext
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
            navigateUp = {},
            navigateNext = {},
            state = UiState.Loading
        )
    }
}