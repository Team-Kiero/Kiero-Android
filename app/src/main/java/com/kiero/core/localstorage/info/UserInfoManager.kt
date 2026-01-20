package com.kiero.core.localstorage.info

interface UserInfoManager {
    suspend fun saveParentInfo(parentName: String, parentProfileImage: String)
    suspend fun getParentInfo(): ParentInfo?
}