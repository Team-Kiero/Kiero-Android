package com.kiero.presentation.parent.screen.mypage.childcare.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mypage.model.ChildConnectionStatus

@Composable
fun ParentMyPageChildStatus(
    connectionStatus: ChildConnectionStatus,
    modifier: Modifier = Modifier,
) {
    val isPending = connectionStatus == ChildConnectionStatus.PENDING
    val statusText = if (isPending) "연결 대기" else "연결 완료"
    val tintColor = if (isPending) KieroTheme.colors.main else KieroTheme.colors.gray300

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_info_circle),
            contentDescription = null,
            tint = tintColor,
            modifier = Modifier
                .size(11.dp)
        )

        Text(
            text = statusText,
            style = KieroTheme.typography.regular.body6,
            color = tintColor,
        )
    }
}

@Preview
@Composable
private fun ParentMyPageChildStatusPreview() {
    KieroTheme {
        ParentMyPageChildStatus(
            connectionStatus = ChildConnectionStatus.CONNECTED
        )
    }
}
