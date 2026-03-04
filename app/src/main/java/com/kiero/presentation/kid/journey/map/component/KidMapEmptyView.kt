package com.kiero.presentation.kid.journey.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMapEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .border(width = 1.dp, color = KieroTheme.colors.main, shape = CircleShape)
                .dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(
                                radius = 8.dp,
                                spread = 0.dp,
                                color = KieroTheme.colors.main,
                                offset = DpOffset(x = 0.dp, y = 0.dp)
                            )
                        )
                .background(color = KieroTheme.colors.gray900, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_fire),
                contentDescription = null,
                tint = KieroTheme.colors.main,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(25.5.dp))

        Text(
            text = "오늘은 등록된 여정이 없어!",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "여정이 없는 오늘 여유를 즐겨봐!",
            color = KieroTheme.colors.gray300,
            style = KieroTheme.typography.regular.body4
        )
    }
}

@Preview
@Composable
private fun KidMapEmptyViewPreview() {
    KieroTheme{
        KidMapEmptyView()
    }
}