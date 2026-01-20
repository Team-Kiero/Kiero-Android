package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ScrollableAutoInputField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "알림장 내용을 입력하세요.",
    maxLength: Int = 1000,
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(text) {
        if (text.isNotEmpty()) {
            delay(100)
            coroutineScope.launch {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val minHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ParentAutoInputField(
                text = text,
                onTextChange = onTextChange,
                placeholder = placeholder,
                maxLength = maxLength,
                maxLines = Int.MAX_VALUE,
                singleLine = false,
                textStyle = KieroTheme.typography.regular.body3,
                textColor = KieroTheme.colors.gray400,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight)
            )

            Spacer(Modifier.height(65.dp))
        }
    }
}