package com.kiero.presentation.parent.screen.schedule.plan.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.disableNestedScroll
import com.kiero.core.common.extension.disableUpWardEvent
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.sonms.wheelpicker.VerticalWheelPicker
import com.sonms.wheelpicker.state.rememberWheelPickerState
import com.sonms.wheelpicker.style.WheelPickerDefaults
import kotlinx.coroutines.launch

@Composable
fun TimePicker(
    value: String,
    placeHolder: String,
    isEditable: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val isEmpty by remember(value) { derivedStateOf { value.isEmpty() } }

    val newModifier =
        modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 39.dp)
            .clip(RoundedCornerShape(9.dp))
            .then(
                if (isEmpty || !isEditable) {
                    Modifier
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = KieroTheme.colors.gray900,
                        shape = RoundedCornerShape(9.dp),
                    )
                },
            )

    Box(
        modifier =
            newModifier
                .background(
                    color = KieroTheme.colors.gray900,
                    RoundedCornerShape(9.dp),
                )
                .noRippleClickable { if (isEditable) onClicked() },
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (isEmpty) placeHolder else value,
                color = if (isEmpty) KieroTheme.colors.gray600 else KieroTheme.colors.gray100,
                style = KieroTheme.typography.regular.body4,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBottomSheet(
    pickerTitle: String,
    initialTime: String,
    onSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val initialValues = remember(initialTime) {
        try {
            val parts = initialTime.split(":", " ")
            Triple(parts[0].toInt(), parts[1].toInt(), parts[2].uppercase())
        } catch (e: Exception) {
            Triple(12, 0, "PM")
        }
    }


    var chosenHour by remember { mutableIntStateOf(initialValues.first) }
    var chosenMinute by remember { mutableIntStateOf(initialValues.second) }
    var chosenAmPm by remember { mutableStateOf(initialValues.third) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = KieroTheme.colors.gray900,
        dragHandle = {},
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .disableNestedScroll()
                .disableUpWardEvent()
        ) {
            PickerTopbar(
                title = pickerTitle,
                leftIconRes = R.drawable.ic_close_light,
                leftIconClick = onDismissRequest,
                rightIconRes = R.drawable.ic_check,
                rightIconClick = {
                    val formattedTime = String.format(
                        "%02d:%02d %s",
                        chosenHour,
                        chosenMinute,
                        chosenAmPm
                    )
                    onSelected(formattedTime)
                    onDismissRequest()
                }
            )

            TimePickerUI(
                chosenHour = chosenHour,
                chosenMinute = chosenMinute,
                chosenAmPm = chosenAmPm,
                onHourChosen = { chosenHour = it },
                onMinuteChosen = { chosenMinute = it },
                onAmPmChosen = { chosenAmPm = it },
            )
        }
    }
}

@Composable
fun TimePickerUI(
    chosenHour: Int,
    chosenMinute: Int,
    chosenAmPm: String,
    onHourChosen: (Int) -> Unit,
    onMinuteChosen: (Int) -> Unit,
    onAmPmChosen: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
    ) {
        TimeSelectionSection(
            chosenHour = chosenHour,
            chosenMinute = chosenMinute,
            chosenAmPm = chosenAmPm,
            onHourChosen = { it.toIntOrNull()?.let { hour -> onHourChosen(hour) } },
            onMinuteChosen = { it.toIntOrNull()?.let { min -> onMinuteChosen(min) } },
            onAmPmChosen = { onAmPmChosen(it) },
        )
    }
}

@Composable
fun TimeSelectionSection(
    chosenHour: Int,
    chosenMinute: Int,
    chosenAmPm: String,
    onHourChosen: (String) -> Unit,
    onMinuteChosen: (String) -> Unit,
    onAmPmChosen: (String) -> Unit,
) {
    val hourIndex = remember(chosenHour) {
        TimePickerConstants.hours.indexOfFirst {
            it == String.format("%02d", chosenHour)
        }.coerceAtLeast(0)
    }
    val minuteIndex = remember(chosenMinute) {
        TimePickerConstants.minutes.indexOfFirst {
            it == String.format("%02d", chosenMinute)
        }.coerceAtLeast(0)
    }
    val amPmIndex = remember(chosenAmPm) {
        TimePickerConstants.amPmList.indexOf(chosenAmPm).coerceAtLeast(0)
    }

    val hourState = rememberWheelPickerState(initialIndex = hourIndex)
    val minuteState = rememberWheelPickerState(initialIndex = minuteIndex)
    val amPmState = rememberWheelPickerState(initialIndex = amPmIndex)

    val scope = rememberCoroutineScope()
    val itemHeight = 44.dp

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(itemHeight)
                .background(
                    color = KieroTheme.colors.gray800,
                    shape = RoundedCornerShape(8.dp),
                )
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            // 시
            VerticalWheelPicker(
                items = TimePickerConstants.hours,
                state = hourState,
                modifier = Modifier.width(70.dp),
                itemHeight = itemHeight,
                visibleItemCount = 7,
                infinite = true,
                style = WheelPickerDefaults.style(
                    selector = WheelPickerDefaults.selectorStyle(
                        background = Color.Transparent,
                        showDivider = false,
                    ),
                ),
                onItemSelected = { _, item -> onHourChosen(item) },
            ) { item, isSelected ->
                TimePickerItem(
                    text = item,
                    isSelected = isSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    onClick = {
                        scope.launch {
                            hourState.animateScrollToIndex(TimePickerConstants.hours.indexOf(item))
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 분
            VerticalWheelPicker(
                items = TimePickerConstants.minutes,
                state = minuteState,
                modifier = Modifier.width(70.dp),
                itemHeight = itemHeight,
                visibleItemCount = 7,
                infinite = true,
                style = WheelPickerDefaults.style(
                    selector = WheelPickerDefaults.selectorStyle(
                        background = Color.Transparent,
                        showDivider = false,
                    ),
                ),
                onItemSelected = { _, item -> onMinuteChosen(item) },
            ) { item, isSelected ->
                TimePickerItem(
                    text = item,
                    isSelected = isSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    onClick = {
                        scope.launch {
                            minuteState.animateScrollToIndex(TimePickerConstants.minutes.indexOf(item))
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // AM/PM
            VerticalWheelPicker(
                items = TimePickerConstants.amPmList,
                state = amPmState,
                modifier = Modifier.width(70.dp),
                itemHeight = itemHeight,
                visibleItemCount = 2,
                infinite = false,
                style = WheelPickerDefaults.style(
                    selector = WheelPickerDefaults.selectorStyle(
                        background = Color.Transparent,
                        showDivider = false,
                    ),
                ),
                onItemSelected = { _, item -> onAmPmChosen(item) },
            ) { item, isSelected ->
                TimePickerItem(
                    text = item,
                    isSelected = isSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    onClick = {
                        scope.launch {
                            amPmState.animateScrollToIndex(TimePickerConstants.amPmList.indexOf(item))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TimePickerItem(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.noRippleClickable(onClick = onClick),
    ) {
        Text(
            text = text,
            color = if (isSelected) KieroTheme.colors.white else KieroTheme.colors.gray600,
            style = KieroTheme.typography.semiBold.title2,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimePickerPreview() {
    KieroTheme {
        TimePicker(
            value = "09:30 AM",
            onClicked = {},
            placeHolder = "시간을 입력해주세요.",
            isEditable = true,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2C2C2E)
@Composable
private fun CustomTimePickerPreview() {
    val chosenHour = remember { mutableIntStateOf(9) }
    val chosenMinute = remember { mutableIntStateOf(30) }
    val chosenAmPm = remember { mutableStateOf("AM") }
    KieroTheme {
        TimePickerUI(
            chosenHour = chosenHour.intValue,
            chosenMinute = chosenMinute.intValue,
            chosenAmPm = chosenAmPm.value,
            onHourChosen = { chosenHour.intValue = it },
            onMinuteChosen = { chosenMinute.intValue = it },
            onAmPmChosen = { chosenAmPm.value = it },
        )
    }
}
