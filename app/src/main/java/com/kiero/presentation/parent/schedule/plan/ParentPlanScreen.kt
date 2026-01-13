package com.kiero.presentation.parent.schedule.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.plan.component.plan.ScheduleDatebar
import com.kiero.presentation.parent.schedule.plan.component.plan.ScheduleTimeTable

@Composable
fun ParentPlanScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = KieroTheme.colors.black
            )
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        
        ScheduleDatebar(
            date = "12월 4주차",
            onPreviousClick = {},
            onNextClick = {}
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = KieroTheme.colors.black
                ),
        ) {
            item {
                ScheduleTimeTable()
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun ParentPlanScreenPreview() {
    KieroTheme {
        ParentPlanScreen()
    }
}