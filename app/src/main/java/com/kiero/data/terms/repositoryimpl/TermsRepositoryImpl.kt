package com.kiero.data.terms.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.terms.model.TermsAgreementModel
import com.kiero.data.terms.model.TermsItemModel
import com.kiero.data.terms.model.TermsStatusModel
import com.kiero.data.terms.model.toModel
import com.kiero.data.terms.remote.datasource.TermsDataSource
import com.kiero.data.terms.repository.TermsRepository
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val termsDataSource: TermsDataSource
) : TermsRepository {
    override suspend fun getTermsStatus(): Result<TermsStatusModel> = suspendRunCatching {
        termsDataSource.getTermsStatus().data!!.toModel()
    }

    override suspend fun getTermsLink(): Result<List<TermsItemModel>> = suspendRunCatching {
        termsDataSource.getTermsLink().data!!.map { it.toModel() }
    }

    override suspend fun postTermsStatus(request: TermsAgreementModel): Result<Unit> = suspendRunCatching {
        termsDataSource.postTermsStatus(request.toDto())
    }
}