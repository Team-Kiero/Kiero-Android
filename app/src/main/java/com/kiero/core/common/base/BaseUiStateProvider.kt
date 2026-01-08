package com.kiero.core.common.base

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.kiero.core.model.UiState

open class BaseUiStateProvider<T>(
    successData: T
) : PreviewParameterProvider<UiState<T>> {
    override val values: Sequence<UiState<T>> = sequenceOf(
        UiState.Loading,
        UiState.Success(successData),
        UiState.Failure("네트워크 연결을 확인해주세요. (테스트 에러)")
    )
}
