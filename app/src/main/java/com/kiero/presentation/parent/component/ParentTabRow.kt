package com.kiero.presentation.parent.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.model.TabItem


@Composable
fun ParentTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    val titles = TabItem.entries.map { it.title }

    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = KieroTheme.colors.gray900,
        contentColor = KieroTheme.colors.white,
        modifier = modifier
            .fillMaxWidth(),
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = false),
                width = Dp.Unspecified,
                height = 1.dp,
                color = KieroTheme.colors.white,
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
            )
        },
        divider = {}
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                },
                text = {
                    Text(
                        text = title,
                        style = KieroTheme.typography.bold.headLine2,
                        color = if (selectedTabIndex == index) KieroTheme.colors.white else KieroTheme.colors.gray500
                    )
                },
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ParentTabRowPreview() {
    ParentTabRow(
        selectedTabIndex = 0,
        onTabSelected = {}
    )
}