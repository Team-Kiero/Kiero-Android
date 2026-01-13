package com.kiero.presentation.parent.schedule.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.plan.component.plan.ScheduleDatebar
import com.kiero.presentation.parent.schedule.plan.component.select.ScheduleTextField
import com.kiero.presentation.parent.schedule.plan.component.select.WeekSelectArea
import com.kiero.presentation.parent.schedule.screen.component.ColorSelectArea
import com.kiero.presentation.parent.schedule.screen.component.TimeSelectArea


@Composable
fun ParentScheduleAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    ScheduleAddScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp
    )
}


@Composable
fun ScheduleAddScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textState = rememberTextFieldState()
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        KieroTopbar(
            title = "일정 추가",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = R.drawable.ic_check,
            leftIconClick = navigateUp,
            rightIconClick = { }
        )

        ScheduleDatebar(
            date = "12.8(월) - 12.14(일)",
            onPreviousClick = { },
            onNextClick = { }
        )

        ScheduleTextField(
            state = textState,
            placeholder = "이름을 입력해주세요",
        )

        WeekSelectArea()

        TimeSelectArea()

        Spacer(modifier = Modifier.height(8.dp))

        ColorSelectArea(
            selectColor = selectedColor,
            onColorSelected = { color ->
                selectedColor = color
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentAddPlanScreenPreview() {
    KieroTheme {
        ScheduleAddScreen(
            paddingValues = PaddingValues(),
            navigateUp = {}
        )
    }
}