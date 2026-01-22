package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ScrollableAutoInputField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "알림장 내용을 입력하세요.",
    maxLength: Int = 1000,
) {
    val scrollState = rememberScrollState()
    val isFocused = remember { mutableStateOf(false) }
    val isImeVisible = WindowInsets.isImeVisible
    var currentSelection by remember { mutableStateOf(TextRange(text.length)) }
    val previousLength = remember { mutableIntStateOf(text.length) }
    val shouldScrollToBottom = remember { mutableStateOf(false) }

    LaunchedEffect(text) {
        val lengthDiff = text.length - previousLength.intValue
        previousLength.intValue = text.length
        if (lengthDiff > 5) {
            shouldScrollToBottom.value = true
        }
    }

    LaunchedEffect(shouldScrollToBottom.value) {
        if (shouldScrollToBottom.value) {
            delay(100)
            scrollState.animateScrollTo(scrollState.maxValue)
            shouldScrollToBottom.value = false
        }
    }

    LaunchedEffect(isImeVisible, isFocused.value) {
        if (isImeVisible && isFocused.value) {
            delay(350)
            val isCursorAtBottom = currentSelection.start == text.length

            if (scrollState.value > 0 && isCursorAtBottom) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val minHeight = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ParentAutoInputField(
                text = text,
                onTextChange = onTextChange,
                onSelectionChange = { range ->
                    currentSelection = range
                },
                placeholder = placeholder,
                maxLength = maxLength,
                maxLines = Int.MAX_VALUE,
                singleLine = false,
                textStyle = KieroTheme.typography.regular.body3,
                textColor = KieroTheme.colors.gray400,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight)
                    .onFocusChanged { focusState ->
                        isFocused.value = focusState.isFocused
                    }
            )

            Spacer(Modifier.height(65.dp))
        }
    }
}