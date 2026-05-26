package com.kiero.presentation.kid.myspace.wisharchive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidHeader
import com.kiero.presentation.kid.myspace.wisharchive.component.KidMySpaceWishArchiveItem
import com.kiero.presentation.kid.myspace.wisharchive.state.KidMySpaceWishArchiveState
import kotlinx.collections.immutable.persistentListOf

@Composable
fun KidMySpaceWishArchiveRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToWish: () -> Unit,
    viewModel: KidMySpaceWishArchiveViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    KidMySpaceWishArchiveScreen(
        paddingValues = paddingValues,
        state = state,
        onConfirmClick = navigateUp,
        onNavigateToWishClick = navigateToWish
    )
}

@Composable
private fun KidMySpaceWishArchiveScreen(
    paddingValues: PaddingValues,
    state: KidMySpaceWishArchiveState,
    onConfirmClick: () -> Unit,
    onNavigateToWishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            KieroTopbar(
                title = "소원의 공간",
                leftIconRes = R.drawable.ic_arrow_left,
                leftIconClick = onConfirmClick,
            )

            Spacer(modifier = Modifier.height(24.dp))

            KidHeader(
                count = state.totalCount,
                isSchedule = false,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Text(
                            text = "오늘 빌었던 소원",
                            style = KieroTheme.typography.regular.body4,
                            color = KieroTheme.colors.white
                        )
                        Spacer(modifier = Modifier.height(11.dp))
                    }

                    if (state.todayWishes.isEmpty()) {
                        item {
                            KidMySpaceWishArchiveItem(
                                hasWish = false,
                                isToday = true,
                                onActionClick = onNavigateToWishClick
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    } else {
                        items(
                            items = state.todayWishes,
                            key = { it.id }
                        ) { wish ->
                            KidMySpaceWishArchiveItem(
                                hasWish = true,
                                isToday = true,
                                title = wish.name,
                                date = wish.acquiredAt,
                                price = wish.price
                            )
                            Spacer(modifier = Modifier.height(11.dp))
                        }
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }

                    if (state.previousWishes.isNotEmpty()) {
                        item {
                            Text(
                                text = "이전소원",
                                style = KieroTheme.typography.regular.body4,
                                color = KieroTheme.colors.white
                            )
                            Spacer(modifier = Modifier.height(11.dp))
                        }

                        items(
                            items = state.previousWishes,
                            key = { it.id }
                        ) { wish ->
                            KidMySpaceWishArchiveItem(
                                hasWish = true,
                                isToday = false,
                                title = wish.name,
                                date = wish.acquiredAt,
                                price = wish.price
                            )
                            Spacer(modifier = Modifier.height(11.dp))
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(54.dp)
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

            KieroButtonMedium(
                text = "확인",
                onClick = onConfirmClick,
                containerColor = KieroTheme.colors.main,
                contentColor = KieroTheme.colors.gray900,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 57.dp)
            )
        }
    }
}

@Preview(name = "스크롤 (소원 있음)")
@Composable
private fun KidMySpaceWishArchiveScreenScrollPreview() {
    KieroTheme {
        KidMySpaceWishArchiveScreen(
            paddingValues = PaddingValues(),
            state = KidMySpaceWishArchiveState.FAKE,
            onConfirmClick = {},
            onNavigateToWishClick = {}
        )
    }
}

@Preview(name = "오늘 소원 없음")
@Composable
private fun KidMySpaceWishArchiveScreenNoTodayPreview() {
    KieroTheme {
        KidMySpaceWishArchiveScreen(
            paddingValues = PaddingValues(),
            state = KidMySpaceWishArchiveState.FAKE.copy(todayWishes = persistentListOf()),
            onConfirmClick = {},
            onNavigateToWishClick = {}
        )
    }
}

@Preview(name = "소원 전혀 없음")
@Composable
private fun KidMySpaceWishArchiveScreenEmptyPreview() {
    KieroTheme {
        KidMySpaceWishArchiveScreen(
            paddingValues = PaddingValues(),
            state = KidMySpaceWishArchiveState.FAKE_EMPTY,
            onConfirmClick = {},
            onNavigateToWishClick = {}
        )
    }
}