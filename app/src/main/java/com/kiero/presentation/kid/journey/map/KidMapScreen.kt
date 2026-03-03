package com.kiero.presentation.kid.journey.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.map.viewModel.KidMapViewModel

@Composable
fun KidMapRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidMapViewModel = hiltViewModel()
) {
    KidMapScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        date = viewModel.date
    )
}

@Composable
private fun KidMapScreen (
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    date: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        KieroTopbar(
            title = date,
            leftIconRes = R.drawable.ic_arrow_left,
            leftIconClick = navigateUp,
        )
    }
}

@Preview
@Composable
private fun KidMapScreenPreview() {
    KieroTheme{
        KidMapScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            date = "12월 5일 목요일"
        )
    }
}