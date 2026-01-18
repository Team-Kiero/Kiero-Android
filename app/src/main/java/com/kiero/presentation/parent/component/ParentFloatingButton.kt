package com.kiero.presentation.parent.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.missionmain.MissionFabContent

@Composable
fun ParentFloatingButton(
    buttonColor: Color,
    onActiveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onActiveClick,
        containerColor = buttonColor,
        modifier = modifier,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_fab_plus),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Composable
fun MissionTabFab(
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onMissionAdd: () -> Unit,
    onMissionRecommend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MissionFabContent(
                    fabTitle = "미션 직접 입력하기",
                    fabIconRes = R.drawable.ic_parent_addschedule_mission,
                    onFabClick = {
                        onExpandedChange(false)
                        onMissionAdd()
                    }
                )

                MissionFabContent(
                    fabTitle = "알림장 한 번에 입력하기",
                    fabIconRes = R.drawable.ic_parent_addschedule_notice,
                    onFabClick = {
                        onExpandedChange(false)
                        onMissionRecommend()
                    }
                )
            }
        }

        ParentFloatingButton(
            buttonColor = KieroTheme.colors.main,
            onActiveClick = { onExpandedChange(!isExpanded) },
            modifier = modifier
                .padding(top = 87.dp)
        )
    }
}

@Composable
fun PlanTabFab(
    onScheduleAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ParentFloatingButton(
        buttonColor = KieroTheme.colors.white,
        onActiveClick = onScheduleAdd,
        modifier = modifier
            .padding(end = 16.dp, bottom = 87.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ParentFloatingButtonPreview() {
    ParentFloatingButton(
        buttonColor = KieroTheme.colors.white,
        onActiveClick = {}
    )
}