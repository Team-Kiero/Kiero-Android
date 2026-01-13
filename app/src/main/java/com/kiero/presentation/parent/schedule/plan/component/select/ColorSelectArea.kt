package com.kiero.presentation.parent.schedule.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.plan.component.picker.ColorPickerBottomSheet

@Composable
fun ColorSelectArea(
    selectColor: Color?,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showColorPicker by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Unspecified)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .noRippleClickable { showColorPicker = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "컬러",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3
        )

        Spacer(modifier = Modifier.weight(1f))

        if (selectColor != null) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        color = selectColor,
                        shape = RoundedCornerShape(5.dp)
                    )
            )

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_addschedule_arrow_right),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.padding(vertical = 3.dp)
        )
    }

    if (showColorPicker) {
        ColorPickerBottomSheet(
            selectedColor = selectColor ?: Color(0xFFCFFFFA),
            onColorSelected = onColorSelected,
            onDismissRequest = { showColorPicker = false }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun PreviewColorSelectAreaPreivew() {
    KieroTheme {
        ColorSelectArea(
            selectColor = null,
            onColorSelected = {}
        )
    }
}