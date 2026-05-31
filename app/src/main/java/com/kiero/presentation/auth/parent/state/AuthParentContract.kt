package com.kiero.presentation.auth.parent.state

import androidx.compose.runtime.Immutable
import com.kiero.core.model.UiState
import com.kiero.presentation.auth.parent.model.TermsType
import com.kiero.presentation.auth.parent.model.TermsUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AuthParentState(
    val uiState: UiState<Unit> = UiState.Empty,
    val isShowTermsAgreement: Boolean = false,
    val termsList: ImmutableList<TermsUiModel> = persistentListOf()
) {
    private val validTerms = termsList.filter { it.termsType != TermsType.UNKNOWN }

    val isAllAgreed: Boolean = validTerms.isNotEmpty() && validTerms.all { it.isAgreed }
}
