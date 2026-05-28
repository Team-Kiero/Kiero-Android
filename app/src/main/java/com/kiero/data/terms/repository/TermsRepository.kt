package com.kiero.data.terms.repository

import com.kiero.data.terms.model.TermsAgreementModel
import com.kiero.data.terms.model.TermsItemModel
import com.kiero.data.terms.model.TermsStatusModel

interface TermsRepository {
    suspend fun getTermsStatus(): Result<TermsStatusModel>
    suspend fun getTermsLink(): Result<List<TermsItemModel>>
    suspend fun postTermsStatus(request: TermsAgreementModel) : Result<Unit>
}