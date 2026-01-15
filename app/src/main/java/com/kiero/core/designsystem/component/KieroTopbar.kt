package com.kiero.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun KieroTopbar(
    modifier: Modifier = Modifier,
    title: String,
    leftIconRes: Int,
    leftIconClick: () -> Unit,
    rightIconRes: Int? = null,
    rightIconClick: (() -> Unit)? = null,
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        // 왼쪽 아이콘
        Icon(
            imageVector = ImageVector.vectorResource(id = leftIconRes),
            contentDescription = null,
            tint = KieroTheme.colors.white,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .noRippleClickable(onClick = leftIconClick)
        )

        // 가운데 텍스트
        Text(
            text = title,
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.bold.headLine2,
            modifier = Modifier.align(Alignment.Center)
        )

        // 오른쪽 아이콘 있을 때만 표시
        if (rightIconRes != null && rightIconClick != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = rightIconRes),
                contentDescription = null,
                tint = KieroTheme.colors.white,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .noRippleClickable(onClick = rightIconClick)
            )
        }
    }
}


@Preview
@Composable
private fun KieroTopbarReview() {
    KieroTheme {
        KieroTopbar(
            title = "알람",
            leftIconRes = R.drawable.ic_arrow_left,
            rightIconRes = R.drawable.ic_arrow_right,
            leftIconClick = {},
            rightIconClick = {}
        )
    }
}