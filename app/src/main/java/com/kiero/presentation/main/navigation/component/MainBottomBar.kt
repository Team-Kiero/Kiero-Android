package com.kiero.presentation.main.navigation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.dropShadow
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MainBottomBar(
    isVisible: Boolean,
    containerShape: Shape,
    tabs: ImmutableList<BottomBarTab>,
    currentTab: BottomBarTab?,
    onTabSelected: (BottomBarTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (isVisible) {
                        Modifier.dropShadow(
                            shape = RoundedCornerShape(12.dp),
                            color = KieroTheme.colors.gray800,
                            offsetX = 0.dp,
                            offsetY = 4.dp,
                            blur = 10.dp,
                            spread = 0.dp
                        )
                    } else {
                        Modifier
                    }
                )
        ) {
            Surface(
                color = KieroTheme.colors.black,
                shape = containerShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEach { tab ->
                        MainNavigationBarItem(
                            selected = tab == currentTab,
                            tab = tab,
                            onClick = { onTabSelected(tab) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainNavigationBarItem(
    selected: Boolean,
    tab: BottomBarTab,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (iconColor, textColor) = if (selected) {
        KieroTheme.colors.white to KieroTheme.colors.white
    } else {
        KieroTheme.colors.gray800 to KieroTheme.colors.gray800
    }

    Column(
        modifier = modifier
            .noRippleClickable(onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(tab.iconRes),
            contentDescription = stringResource(tab.contentDescription),
            tint = iconColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(tab.labelRes),
            color = textColor,
            style = KieroTheme.typography.regular.body5,
            textAlign = TextAlign.Center
        )
    }
}