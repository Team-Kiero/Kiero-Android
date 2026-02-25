package com.kiero.presentation.parent.screen.schedule.plan.component.select

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.schedule.plan.component.picker.TimePickerBottomSheet

@Composable
fun TimeSelectArea(
    startTime: String,
    endTime: String,
    onTimeSelected: (Boolean, String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "시간",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3,
            modifier = Modifier
                .padding(vertical = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            TimeBlock(
                onClick = { showStartTimePicker = true },
                selectTime = startTime,
                label = "시작",
                isEnabled = showStartTimePicker,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            TimeBlock(
                onClick = { showEndTimePicker = true },
                selectTime = endTime,
                label = "종료",
                isEnabled = showEndTimePicker,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showStartTimePicker) {
        TimePickerBottomSheet(
            pickerTitle = "시작",
            initialTime = startTime,
            onSelected = { time ->
                onTimeSelected(true, time)
                showStartTimePicker = false
            },
            onDismissRequest = { showStartTimePicker = false }
        )
    }

    if (showEndTimePicker) {
        TimePickerBottomSheet(
            pickerTitle = "종료",
            initialTime = endTime,
            onSelected = { time ->
                onTimeSelected(false, time)
                showEndTimePicker = false
            },
            onDismissRequest = { showEndTimePicker = false }
        )
    }
}


@Composable
private fun TimeBlock(
    onClick: () -> Unit,
    selectTime: String,
    label: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
) {
    val (textColor, borderColor) = when (isEnabled) {
        true -> KieroTheme.colors.main to KieroTheme.colors.main
        else -> KieroTheme.colors.white to KieroTheme.colors.gray900
    }
    Column(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .background(
                color = Color.Unspecified,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                vertical = 15.dp,
                horizontal = 10.dp
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp),

        ) {
        Text(
            text = label,
            color = textColor,
            style = KieroTheme.typography.regular.body5
        )

        Text(
            text = selectTime,
            color = textColor,
            style = KieroTheme.typography.semiBold.title2
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun TimeSelectAreaPreview() {
    KieroTheme {
//        TimeSelectArea(
//            startTime = "12:00",
//            endTime = "15:00",
//            onTimeSelected = (true,"")
//        )
    }
}