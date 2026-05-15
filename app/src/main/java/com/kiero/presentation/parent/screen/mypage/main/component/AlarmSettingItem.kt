package com.kiero.presentation.parent.screen.mypage.main.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.switch.KieroAlarmSwitch
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun AlarmSettingItem(
    text: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.white,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        KieroAlarmSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Preview
@Composable
private fun AlarmSettingItemPreview() {
    KieroTheme {
        AlarmSettingItem(
            text = "푸시 알림",
            checked = true,
            onCheckedChange = {},
            enabled = true
        )
    }
}
