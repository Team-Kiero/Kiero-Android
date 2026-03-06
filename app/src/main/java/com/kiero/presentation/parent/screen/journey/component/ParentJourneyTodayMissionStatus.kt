package com.kiero.presentation.parent.screen.journey.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

// Todo: 서버 기준으로 변경하기
@Composable
fun ParentJourneyTodayMissionStatus(
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .dropShadow(
                shape = RoundedCornerShape(15.dp),
                shadow = Shadow(
                    radius = 4.dp,
                    spread = 4.dp,
                    alpha = 0.15f,
                    color = Color(0xff000000),
                    offset = DpOffset(x = 0.dp, y = 4.dp)
                )
            )
            .background(
                color = KieroTheme.colors.black,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ParentJourneyTodayMissionItem(
            itemTitle = "완료 미션",
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = KieroTheme.colors.gray800,
            thickness = 1.dp
        )

        ParentJourneyTodayMissionItem(
            itemTitle = "미완료 미션",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ParentJourneyTodayMissionItem(
    itemTitle: String,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = itemTitle,
            style = KieroTheme.typography.regular.body5,
            color = KieroTheme.colors.gray500
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "2개",
            style = KieroTheme.typography.semiBold.title4,
            color = KieroTheme.colors.white
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = KieroTheme.colors.white
        )
    }
}

@Preview
@Composable
private fun ParentJourneyTodayMissionStatusPreview() {
    KieroTheme {
        ParentJourneyTodayMissionStatus()
    }
}
