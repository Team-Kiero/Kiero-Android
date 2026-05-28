package com.kiero.core.localstorage.info

import com.kiero.core.model.parent.ParentInfo

interface UserInfoManager {
    suspend fun saveParentInfo(parentName: String, parentProfileImage: String)
    suspend fun getParentInfo(): ParentInfo?

    suspend fun clearParentInfo()

    suspend fun saveChildIdInfo(childId : Long)
    suspend fun getChildIdInfo(): Long?

    /**
     * 필수 약관 동의 여부 저장
     * */
    suspend fun saveTermsInfo(isRequiredTermsAllAgreed: Boolean)

    suspend fun getTermsInfo(): Boolean?

    suspend fun saveAgreedTermsIds(termsIds: List<Long>)
    suspend fun getAgreedTermsIds(): List<Long>?

    /**
    * 자녀 정보 - 발급 코드, 남은 시간 등
    * */
    suspend fun saveChildName(lastName: String, firstName: String)
    suspend fun getChildLastName(): String?
    suspend fun getChildFirstName(): String?

    suspend fun savePendingInviteCode(code: String, expireTimeMillis: Long)
    suspend fun getPendingInviteCode(): String?
    suspend fun getPendingInviteExpireTime(): Long
    suspend fun clearPendingInviteCode()
}
