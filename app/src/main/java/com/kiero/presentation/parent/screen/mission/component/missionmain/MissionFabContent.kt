package com.kiero.presentation.parent.screen.mission.component.missionmain

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MissionFabContent(
    fabTitle: String,
    @DrawableRes fabIconRes: Int,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(20.dp)
            )
            .noRippleClickable(onClick = onFabClick)
            .padding(
                horizontal = 15.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = fabTitle,
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title4,
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = fabIconRes),
            contentDescription = null,
            tint = Color.Unspecified
        )

    }
}

@Preview
@Composable
private fun MissionFabContentPreview() {
    KieroTheme {
        MissionFabContent(
            fabTitle = "미션 직접 입력하기",
            fabIconRes = R.drawable.ic_parent_addschedule_mission,
            onFabClick = {}
        )
    }
}