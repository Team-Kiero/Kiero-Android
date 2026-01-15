package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentMissionNavigator(
    currentIndex: Int,
    totalCount: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        // 왼쪽 아이콘
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
            contentDescription = "이전",
            tint = if (currentIndex > 0) KieroTheme.colors.white else KieroTheme.colors.gray700,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .then(
                    if (currentIndex > 0) {
                        Modifier.noRippleClickable(onClick = onPreviousClick)
                    } else {
                        Modifier
                    }
                )
        )

        // 중앙 텍스트
        Text(
            text = "미션 ${currentIndex + 1}/$totalCount",
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.white,
            textAlign = TextAlign.Center
        )

        // 오른쪽 아이콘
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = "다음",
            tint = if (currentIndex < totalCount - 1) KieroTheme.colors.white else KieroTheme.colors.gray700,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .then(
                    if (currentIndex < totalCount - 1) {
                        Modifier.noRippleClickable(onClick = onNextClick)
                    } else {
                        Modifier
                    }
                )
        )
    }
}

@Preview
@Composable
private fun ParentMissionNavigatorPreview() {
    KieroTheme {
        ParentMissionNavigator(
            currentIndex = 0,
            totalCount = 5,
            onPreviousClick = {},
            onNextClick = {}
        )
    }
}