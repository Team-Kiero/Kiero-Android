package com.kiero.presentation.parent.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mission.component.missionmain.MissionFabContent

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
    Box(modifier = modifier.zIndex(1f)) {
        if (isExpanded) {
            Popup(
                onDismissRequest = { onExpandedChange(false) },
                properties = PopupProperties(
                    focusable = true,
                    excludeFromSystemGesture = true
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KieroTheme.colors.black.copy(alpha = 0.75f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onExpandedChange(false)
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 52.dp)
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
                            onActiveClick = { onExpandedChange(false) },
                            modifier = Modifier.padding(end = 16.dp, bottom = 51.dp)
                        )
                    }
                }
            }
        }

        if (!isExpanded) {
            ParentFloatingButton(
                buttonColor = KieroTheme.colors.main,
                onActiveClick = { onExpandedChange(true) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 51.dp)
            )
        }
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