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

// Todo : 서버 SSE 또는 API 구현 시 status 변경
@Composable
fun ParentMyPageChildStatus(
    modifier: Modifier = Modifier,
    isChildJoined: Boolean = false,
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_info_circle),
            contentDescription = null,
            tint = if (isChildJoined) KieroTheme.colors.gray300 else KieroTheme.colors.main,
            modifier = Modifier
                .size(11.dp)
        )

        Text(
            text = if (isChildJoined) "연결 완료" else "연결 대기",
            style = KieroTheme.typography.regular.body6,
            color = if (isChildJoined) KieroTheme.colors.gray300 else KieroTheme.colors.main,
        )
    }
}

@Preview
@Composable
private fun ParentMyPageChildStatusPreview() {
    KieroTheme {
        ParentMyPageChildStatus()
    }
}
