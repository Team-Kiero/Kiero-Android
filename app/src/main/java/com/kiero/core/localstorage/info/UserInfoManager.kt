package com.kiero.core.localstorage.info

import com.kiero.core.model.parent.ParentInfo

interface UserInfoManager {
    suspend fun saveParentInfo(parentName: String, parentProfileImage: String)
    suspend fun getParentInfo(): ParentInfo?

    suspend fun clearParentInfo()

    suspend fun saveChildIdInfo(childId : Long)
    suspend fun getChildIdInfo(): Long?
}
