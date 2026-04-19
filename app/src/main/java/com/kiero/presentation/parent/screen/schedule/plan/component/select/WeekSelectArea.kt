package com.kiero.presentation.parent.screen.schedule.plan.component.select

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WeekSelectArea(
    selectedDays: Set<Int>,
    isRecurring: Boolean,
    isEditMode: Boolean = false,
    onDayClick: (Int) -> Unit,
    onAllDaysSelect: (Boolean) -> Unit,
    onRecurringToggle: () -> Unit,
    onShowToast: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            )
            .padding(
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        DaySection(
            onValidClick = {
                if (isEditMode) onShowToast("요일은 수정할 수 없어요. 삭제 후 등록해주세요.")
                else onRecurringToggle()
            },
            isEnabled = isRecurring
        )

        DayPickSection(
            selectedDays = selectedDays,
            isEditMode = isEditMode,
            onShowToast = onShowToast,
            onDayClick = onDayClick
        )

        RepeatSection(
            onAbleClick = { isEnabled ->
                if (isEditMode) onShowToast("요일은 수정할 수 없어요. 삭제 후 등록해주세요.")
                else onAllDaysSelect(isEnabled)
            },
            isEnabled = selectedDays.size == 7
        )
    }
}

@Composable
private fun DayBox(
    isSelectClick: () -> Unit,
    dayTitle: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
    isEditMode: Boolean = false,
    onShowToast: (String) -> Unit = {}
) {
    // 🔥 선택(isEnabled)된 요일이면서 수정 모드(isEditMode)일 경우 무조건 흰색(Color.White) 적용
    val (borderColor, textColor) = when {
        isEnabled && isEditMode -> KieroTheme.colors.white to KieroTheme.colors.white
        isEnabled -> KieroTheme.colors.main to KieroTheme.colors.main
        else -> Color.Unspecified to KieroTheme.colors.gray700
    }

    Text(
        text = dayTitle,
        color = textColor,
        style = KieroTheme.typography.regular.body1,
        textAlign = TextAlign.Center,
        modifier = modifier
            .noRippleClickable(onClick = {
                // 🔥 내부 클릭 막기
                if (isEditMode) onShowToast("요일 및 반복 여부는 수정할 수 없어요. 삭제 후 등록해주세요.")
                else isSelectClick()
            })
            .background(
                color = KieroTheme.colors.gray900,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape
            )
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val maxSize = maxOf(placeable.width, placeable.height)

                layout(maxSize, maxSize) {
                    placeable.placeRelative(
                        x = (maxSize - placeable.width) / 2,
                        y = (maxSize - placeable.height) / 2
                    )
                }
            }
            .padding(8.dp)
    )
}

@Composable
private fun DayPickSection(
    selectedDays: Set<Int>,
    isEditMode: Boolean,
    onShowToast: (String) -> Unit,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    dayList: ImmutableList<String> = persistentListOf("월", "화", "수", "목", "금", "토", "일"),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            )
            .padding(horizontal = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dayList.forEachIndexed { index, day ->
            DayBox(
                isSelectClick = { onDayClick(index) },
                dayTitle = day,
                modifier = Modifier.weight(1f),
                isEnabled = selectedDays.contains(index),
                isEditMode = isEditMode,
                onShowToast = onShowToast
            )
        }
    }
}

@Composable
private fun RepeatSection(
    onAbleClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
) {
    val iconRes =
        if (isEnabled) R.drawable.ic_parent_addschedule_check_on
        else R.drawable.ic_parent_addschedule_check_off

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable(onClick = { onAbleClick(isEnabled) })
        )

        Text(
            text = "매일",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.regular.body5
        )
    }
}

@Composable
private fun DaySection(
    onValidClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "요일",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "매주 반복",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.bold.headLine4
        )

        DayToggle(
            onClick = onValidClick,
            isChecked = isEnabled
        )
    }
}

@Composable
private fun DayToggle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
) {
    Switch(
        checked = isChecked,
        onCheckedChange = { onClick() },
        modifier = modifier
            .scale(0.7f),
        colors = SwitchDefaults.colors(
            uncheckedThumbColor = KieroTheme.colors.gray200,
            checkedThumbColor = KieroTheme.colors.white,
            checkedTrackColor = KieroTheme.colors.main,
            uncheckedTrackColor = KieroTheme.colors.gray800,
            checkedBorderColor = Color.Unspecified,
            uncheckedBorderColor = Color.Unspecified,
        ),
        thumbContent = {
            Box(
                modifier = Modifier
                    .size(13.dp)
            )
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun PreviewDayBox() {
    KieroTheme {
        Column {
            DayBox(
                isSelectClick = {},
                dayTitle = "월",
                isEditMode = false
            )
        }
    }
}