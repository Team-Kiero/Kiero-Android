package com.kiero.presentation.parent.schedule.plan.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerBottomSheet(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    var tempSelectedColor by remember { mutableStateOf(selectedColor) }

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
        ) {
            PickerTopbar(
                title = "컬러",
                leftIconRes = R.drawable.ic_close_light,
                leftIconClick = onDismissRequest,
                rightIconRes = R.drawable.ic_check,
                rightIconClick = {
                    onColorSelected(tempSelectedColor)
                    onDismissRequest()
                },
            )

            Spacer(modifier = Modifier.height(25.dp))

            ColorPicker(
                selectedColor = tempSelectedColor,
                onColorSelected = { color ->
                    tempSelectedColor = color
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 46.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ColorType.entries.forEach { colorType ->
            ColorPickerItem(
                selectColor = colorType.color,
                isSelected = selectedColor == colorType.color,
                onClick = { onColorSelected(colorType.color) },
            )
        }
    }
}

@Composable
fun ColorPickerItem(
    selectColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(35.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(
                color = selectColor,
                shape = RoundedCornerShape(5.dp)
            )
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_addschedule_color_check),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

enum class ColorType(val color: Color) {
    SCHEDULE1(Color(0xFFCFFFFA)),
    SCHEDULE2(Color(0xFFFFFEE9)),
    SCHEDULE3(Color(0xFFBFFFE3)),
    SCHEDULE4(Color(0xFF34D9D3)),
    SCHEDULE5(Color(0xFF7BBDFF)),
}

@Preview(showBackground = true, backgroundColor = 0xFF2C2C2E)
@Composable
private fun ColorPickerPreview() {
    KieroTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ColorPicker(
                selectedColor = ColorType.SCHEDULE1.color,
                onColorSelected = {}
            )
        }
    }
}