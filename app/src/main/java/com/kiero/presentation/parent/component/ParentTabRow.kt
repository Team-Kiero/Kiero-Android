package com.kiero.presentation.parent.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = KieroTheme.colors.gray900,
            contentColor = KieroTheme.colors.white,
            modifier = modifier.fillMaxWidth(),
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        selectedTabIndex,
                        matchContentSize = false
                    ),
                    width = Dp.Unspecified,
                    height = 1.dp,
                    color = KieroTheme.colors.white,
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier,
                    text = {
                        Text(
                            text = title,
                            style = KieroTheme.typography.bold.headLine2,
                            color = if (selectedTabIndex == index) {
                                KieroTheme.colors.white
                            } else {
                                KieroTheme.colors.gray500
                            }
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentTabRowPreview() {
    KieroTheme {
        ParentTabRow(
            tabs = listOf("일정", "미션"),
            selectedTabIndex = 0,
            onTabSelected = {}
        )
    }
}