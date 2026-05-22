package com.kiero.presentation.kid.myspace.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMySpaceNotification(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "알림 켜기",
                style = KieroTheme.typography.semiBold.title3,
                color = KieroTheme.colors.white
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "진행되는 여정에 대한 알림을 받을 수 있어!",
                style = KieroTheme.typography.regular.body4,
                color = KieroTheme.colors.gray400
            )
        }

        // Todo: 토글 추가
    }
}

@Preview
@Composable
private fun KidMySpaceNotificationPreview() {
    KieroTheme {
        KidMySpaceNotification(
            isChecked = false,
            onCheckedChange = {}
        )
    }
}