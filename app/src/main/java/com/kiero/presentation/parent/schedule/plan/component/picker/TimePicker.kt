package com.kiero.presentation.parent.schedule.plan.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import kotlinx.collections.immutable.ImmutableList
import java.util.Calendar

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
    onSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()


    val currentCalendar = Calendar.getInstance()
    val currentHour12 = currentCalendar.get(Calendar.HOUR).let { if (it == 0) 12 else it }
    val currentMinute = currentCalendar.get(Calendar.MINUTE)
    val currentAmPm = if (currentCalendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

    var chosenHour by remember { mutableIntStateOf(currentHour12) }
    var chosenMinute by remember { mutableIntStateOf(currentMinute) }
    var chosenAmPm by remember { mutableStateOf(currentAmPm) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = KieroTheme.colors.gray900,
        dragHandle = {},
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, _ -> }
                }
                .padding(vertical = 16.dp)
        ) {
            PickerTopbar(
                title = "시간",
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

            Spacer(modifier = Modifier.height(12.dp))

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
                .padding(vertical = 16.dp, horizontal = 8.dp),
    ) {
        TimeSelectionSection(
            chosenHour = chosenHour,
            chosenMinute = chosenMinute,
            chosenAmPm = chosenAmPm,
            onHourChosen = { onHourChosen(it.toInt()) },
            onMinuteChosen = { onMinuteChosen(it.toInt()) },
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
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    color = KieroTheme.colors.gray800,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TimeItemsPicker(
                items = TimePickerConstants.hours,
                firstIndex = chosenHour + 2 - 1,
                onItemSelected = onHourChosen,
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimeItemsPicker(
                items = TimePickerConstants.minutes,
                firstIndex = chosenMinute + 2,
                onItemSelected = onMinuteChosen,
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimeItemsPicker(
                items = TimePickerConstants.amPmList,
                firstIndex = if (chosenAmPm == "AM") 2 else 3,
                onItemSelected = onAmPmChosen,
            )
        }
    }
}

@Composable
fun TimeItemsPicker(
    items: ImmutableList<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState(firstIndex)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                return available
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return available
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                val selectedIndex = (index + 2) % items.size
                if (items[selectedIndex].isNotEmpty()) {
                    onItemSelected(items[selectedIndex])
                }
            }
    }

    Box(
        modifier = modifier
            .height(210.dp)
            .width(70.dp),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .nestedScroll(nestedScrollConnection),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
        ) {
            items(items.size) {
                val index = it % items.size
                val firstVisibleItemIndex by remember {
                    derivedStateOf { listState.firstVisibleItemIndex }
                }

                val isSelected = it == firstVisibleItemIndex + 2

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = items[index],
                    color = KieroTheme.colors.gray200,
                    modifier = Modifier
                        .alpha(if (isSelected) 1f else 0.5f)
                        .height(36.dp)
                        .padding(vertical = 2.dp),
                    style = KieroTheme.typography.semiBold.title2,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
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