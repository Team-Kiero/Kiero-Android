package com.kiero.presentation.parent.screen.journey.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.disableNestedScroll
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.bottomsheet.KieroBottomSheet
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.component.emptyview.KieroContentEmptyScreen
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.journey.model.JourneyMissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentJourneyBottomSheet(
    completeMissions: ImmutableList<JourneyMissionUiModel>,
    incompleteMissions: ImmutableList<JourneyMissionUiModel>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialTab: Int = 0, // 0 완료, 1 미완료
) {
    var selectedTabIndex by remember { mutableIntStateOf(initialTab) }

    val tabs = persistentListOf("완료", "미완료")

    KieroBottomSheet(
        onDismiss = onDismiss,
        dragHandle = null,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .pointerInput(Unit) {
                    detectTapGestures {
                    }
                }
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 27.dp, bottom = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedTabIndex == 0) "완료 미션" else "미완료 미션",
                    style = KieroTheme.typography.bold.headLine3,
                    color = KieroTheme.colors.white,
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .noRippleClickable(
                            onClick = onDismiss
                        )
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = KieroTheme.colors.gray800,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    ParentJourneyTab(
                        title = title,
                        isSelected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }

            val currentList = if (selectedTabIndex == 0) completeMissions else incompleteMissions

            if (currentList.isEmpty()) {
                KieroContentEmptyScreen(
                    description = if (selectedTabIndex == 0) "아직 완료한 미션이 없어요." else "남은 미션이 없어요! 모두 완료했어요.",
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomHeight = 0.dp
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .disableNestedScroll()
                        .fillMaxWidth(),
                ) {
                    items(
                        items = currentList,
                    ) { mission ->
                        ParentJourneyMissionItem(mission = mission)
                    }
                }
            }
        }
    }
}

@Composable
private fun ParentJourneyMissionItem(
    mission: JourneyMissionUiModel,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 13.dp)
    ) {
        Text(
            text = mission.name,
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.white
        )

        Spacer(modifier = Modifier.weight(1f))

        KieroChip(
            action = KieroCoinAction(
                coinCount = mission.reward,
                onClick = {}
            )
        )
    }
}

@Composable
private fun ParentJourneyTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = KieroTheme.typography.semiBold.title4,
        color = if (isSelected) KieroTheme.colors.main else KieroTheme.colors.gray600,
        modifier = modifier.noRippleClickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun ParentJourneyBottomSheetPreview() {
    KieroTheme {
        ParentJourneyBottomSheet(
            completeMissions = persistentListOf(),
            incompleteMissions = persistentListOf(),
            onDismiss = {},
            initialTab = 0
        )
    }
}
