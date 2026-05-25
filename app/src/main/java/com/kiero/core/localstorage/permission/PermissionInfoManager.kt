package com.kiero.core.localstorage.permission

import com.kiero.core.permission.model.PermissionType
import kotlinx.coroutines.flow.Flow

interface PermissionInfoManager {
    /**
     * 해당 권한의 거부 횟수 1 증가
     * */
    suspend fun increaseDeniedCount(type: PermissionType)

    /**
     * 해당 권한의 거부 횟수 스트림
     * */
    fun deniedCount(type: PermissionType): Flow<Int>
}
