package com.kiero.core.designsystem.component.emptyview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

/** 아이콘이 들어간 컨텐츠 빈 화면*/
@Composable
fun KieroContentEmptyScreen(
    modifier: Modifier = Modifier,
    title: String? = "오늘 등록된 일정이 없어요.",
    description: String = "우측 하단 버튼을 눌러 일정을 추가해보세요!",
    bottomHeight : Dp = 50.dp
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_empty_state),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(11.dp))

            if (title != null) {
                Text(
                    text = title,
                    style = KieroTheme.typography.semiBold.title3,
                    color = KieroTheme.colors.gray500
                )
            }

            Text(
                text = description,
                style = KieroTheme.typography.regular.body4,
                color = KieroTheme.colors.gray700
            )
            Spacer(modifier = Modifier.height(bottomHeight))
        }
    }

}

@Preview
@Composable
private fun KieroContentEmptyScreenPreview() {
    KieroTheme {
        KieroContentEmptyScreen()
    }
}
