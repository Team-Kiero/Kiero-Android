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
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState

@Composable
fun AuthRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
) {
    AuthScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        navigateToParent = navigateToParent,
        navigateToKid = navigateToKid,
        modifier = Modifier
    )
}

@Composable
fun AuthScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
}

@Preview
@Composable
private fun DummyScreenPreview() {
    KieroTheme {
        AuthScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            navigateToParent = {},
            navigateToKid = {}
        )
    }
}