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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroEmptyView(
    modifier: Modifier = Modifier,
    title: String? = "오늘 등록된 일정이 없어요.",
    description: String = "우측 하단 버튼을 눌러 일정을 추가해보세요!",
    bottomHeight : Dp = 172.dp
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_empty_state),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(7.dp))

            if (title != null) {
                Text(
                    text = title,
                    style = KieroTheme.typography.semiBold.title4,
                    color = KieroTheme.colors.gray400
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