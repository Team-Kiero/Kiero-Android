package com.kiero.presentation.auth.parent.model

import androidx.compose.runtime.Immutable
import com.kiero.data.terms.model.TermsItemModel

@Immutable
data class TermsUiModel(
    val termsType: TermsType,
    val termsId: Long,
    val url: String,
    val isAgreed: Boolean = false
)

fun TermsItemModel.toUiModel() = TermsUiModel(
    termsType = TermsType.fromServerString(this.termsType),
    termsId = termsId,
    url = url,
)
