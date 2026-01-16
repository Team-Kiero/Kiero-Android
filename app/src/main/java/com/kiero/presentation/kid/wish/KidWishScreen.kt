package com.kiero.presentation.kid.wish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.wish.component.KidWishDescription
import com.kiero.presentation.kid.wish.component.KidWishGridList
import com.kiero.presentation.kid.wish.model.KidWishUiModel
import com.kiero.presentation.kid.wish.preview.KidWishPreviewProvider
import com.kiero.presentation.kid.wish.state.KidWishState
import com.kiero.presentation.kid.wish.viewmodel.KidWishViewModel
import kotlinx.collections.immutable.PersistentList

@Composable
fun KidWishRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: KidWishViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    with(state) {
        KidWishScreen(
            paddingValues = paddingValues,
            state = state,
            navigateUp = navigateUp
        )
    }
}

@Composable
private fun KidWishScreen(
    paddingValues: PaddingValues,
    state: KidWishState,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = KieroTheme.colors.black
            )
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 25.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KidProfileChip(
                kidName = state.kidName
            )

            Spacer(modifier = Modifier.weight(1f))

            KieroChip(
                action = KieroCoinAction(
                    coinCount = state.coinUiModel.coinAmount,
                    isEnabled = true,
                    onClick = {}
                )
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        KidWishDescription()

        Spacer(modifier = Modifier.height(17.dp))

        HorizontalDivider(
            thickness = 2.dp,
            color = KieroTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.height(17.dp))

        when (state.kidWishList) {
            is UiState.Success -> {
                KidWishGridList(
                    modifier = Modifier.fillMaxWidth(),
                    wishList = state.kidWishList.data
                )
            }

            UiState.Empty -> TODO()
            is UiState.Failure -> TODO()
            UiState.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
@Preview
private fun KidWishScreenPreview(
    @PreviewParameter(KidWishPreviewProvider::class) uiState: UiState<PersistentList<KidWishUiModel>>
) {
    KieroTheme {
        val state = KidWishState(
            kidWishList = uiState
        )

        KidWishScreen(
            paddingValues = PaddingValues(),
            state = state,
            navigateUp = {}
        )
    }
}