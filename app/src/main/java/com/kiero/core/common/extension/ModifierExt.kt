package com.kiero.core.common.extension

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.kiero.core.designsystem.theme.KieroTheme

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

fun Modifier.forcePixelToDp(painter: Painter): Modifier {
    return this.size(
        width = painter.intrinsicSize.width.dp,
        height = painter.intrinsicSize.height.dp
    )
}

fun Modifier.systemBarColor(color: Color): Modifier = composed {
    val activity = LocalActivity.current

    val isDarkIcons = color.luminance() > 0.5f

    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LaunchedEffect(activity, isDarkIcons) {
        val window = activity?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, window.decorView)

        controller.isAppearanceLightStatusBars = isDarkIcons
        controller.isAppearanceLightNavigationBars = isDarkIcons
    }

    this.drawBehind {
        val statusBarHeightPx = with(density) { statusBarHeight.toPx() }
        val navBarHeightPx = with(density) { navBarHeight.toPx() }

        drawRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(size.width, statusBarHeightPx)
        )

        drawRect(
            color = color,
            topLeft = Offset(0f, size.height - navBarHeightPx),
            size = Size(size.width, navBarHeightPx)
        )
    }
}

fun Modifier.statusBarColor(backgroundColor: Color): Modifier = composed {
    val activity = LocalActivity.current
    val isDarkIcons = backgroundColor.luminance() > 0.5f
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LaunchedEffect(activity, isDarkIcons) {
        val window = activity?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = isDarkIcons
    }

    this.drawBehind {
        drawRect(
            color = backgroundColor,
            topLeft = Offset.Zero,
            size = Size(size.width, statusBarHeight.toPx())
        )
    }
}

// LazyColumn 같이 스크롤 후 더 이상 스크롤할 곳이 없을 때 바텀시트 움직임 막기
fun Modifier.disableNestedScroll(): Modifier = composed {
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                return available
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return available
            }
        }
    }
    this.nestedScroll(connection)
}

// 위로 드래그 막는 함수 - 스크롤이 발생하기 전에 위로 올리는 행동 자체를 사전에 차단
fun Modifier.disableUpScroll(): Modifier = composed {
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return if (available.y < 0) available else Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                return if (available.y < 0) {
                    val maxUpwardVelocity = -1000f

                    // 위로 튕기는 속도가 너무 강할 때
                    if (available.y < maxUpwardVelocity) {
                        Velocity(x = 0f, y = available.y - maxUpwardVelocity)
                    } else {
                        Velocity.Zero
                    }
                } else {
                    Velocity.Zero
                }
            }
        }
    }
    this.nestedScroll(connection)
}

// 자식에 LazyColumn 과 같은 스크롤 컴포넌트가 존재하는 경우 바텀시트가 튕기는 현상 방지
fun Modifier.disableUpSheetScroll(): Modifier = composed {
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return if (available.y < 0) available else Offset.Zero
            }

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {
                return if (available.y < 0) {
                    val maxUpwardVelocity = -500f
                    if (available.y < maxUpwardVelocity) {
                        Velocity(x = 0f, y = available.y - maxUpwardVelocity)
                    } else {
                        Velocity.Zero
                    }
                } else {
                    Velocity.Zero
                }
            }
        }
    }
    this.nestedScroll(connection)
}


@Composable
fun Modifier.verticalScrollbar(
    state: LazyListState,
    thickness: Dp = 5.dp,
    thumbHeight: Dp = 40.dp,
    verticalPadding: Dp = 30.dp,
    thumbColor: Color = KieroTheme.colors.white,
    trackColor: Color = KieroTheme.colors.gray900
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val animationAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithCache {
        val thicknessPx = thickness.toPx()
        val thumbHeightPx = thumbHeight.toPx()
        val paddingPx = verticalPadding.toPx()
        val cornerRadius = CornerRadius(thicknessPx / 2f)

        onDrawWithContent {
            drawContent()
            val canScroll = state.canScrollForward || state.canScrollBackward

            if (!canScroll || animationAlpha == 0f || state.layoutInfo.totalItemsCount == 0) {
                return@onDrawWithContent
            }

            val scrollProportion = calculateScrollProportion(state)
            val trackHeight = size.height - (paddingPx * 2)
            val maxScrollOffsetY = trackHeight - thumbHeightPx

            val scrollbarOffsetX = size.width - thicknessPx
            val scrollbarOffsetY = paddingPx + (scrollProportion * maxScrollOffsetY)

            drawRoundRect(
                color = trackColor,
                topLeft = Offset(scrollbarOffsetX, paddingPx),
                size = Size(thicknessPx, trackHeight),
                cornerRadius = cornerRadius,
                alpha = animationAlpha
            )

            drawRoundRect(
                color = thumbColor,
                topLeft = Offset(scrollbarOffsetX, scrollbarOffsetY),
                size = Size(thicknessPx, thumbHeightPx),
                cornerRadius = cornerRadius,
                alpha = animationAlpha
            )
        }
    }
}

private fun calculateScrollProportion(state: LazyListState): Float {
    val layoutInfo = state.layoutInfo
    val totalItems = layoutInfo.totalItemsCount
    val visibleItems = layoutInfo.visibleItemsInfo

    val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val firstVisibleIndex = state.firstVisibleItemIndex
    val firstVisibleOffset = state.firstVisibleItemScrollOffset
    val firstItemSize = visibleItems.firstOrNull { it.index == firstVisibleIndex }?.size?.toFloat() ?: 1f

    val averageItemSize = if (visibleItems.isNotEmpty()) {
        visibleItems.sumOf { it.size }.toFloat() / visibleItems.size
    } else {
        firstItemSize
    }

    val exactIndex = firstVisibleIndex.toFloat() + (firstVisibleOffset.toFloat() / firstItemSize)

    val maxExactIndex = (totalItems - viewportHeight.toFloat() / averageItemSize).coerceAtLeast(0.1f) + 1f

    return (exactIndex / maxExactIndex).coerceIn(0f, 1f)
}