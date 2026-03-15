package com.kiero.presentation.kid.journey.map.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.verticalScrollbar
import com.kiero.presentation.kid.journey.map.model.KidMapItemUiModel

@Composable
fun KidMapScheduleList(
    schedules: List<KidMapItemUiModel>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier.verticalScrollbar(state = listState),
        contentPadding = PaddingValues(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        itemsIndexed(
            items = schedules,
            key = { index, _ -> index }
        ) { _, item ->
            KidMapListItem(item = item)
        }
    }
}