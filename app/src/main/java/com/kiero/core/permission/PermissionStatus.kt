package com.kiero.core.permission

sealed interface PermissionStatus {
    /** 권한 허용됨 */
    data object Granted : PermissionStatus

    /**
     * 아직 요청 가능. 시스템 다이얼로그를 띄울 수 있는 상태.
     * shouldShowRationale == true 이거나, 아직 한 번도 요청 안 한 초기 상태.
     */
    data class ShouldRequest(val showRationale: Boolean) : PermissionStatus

    /**
     * 영구 거부. 시스템 다이얼로그가 더 이상 안 뜸 → 설정으로 보내야 함.
     */
    data object PermanentlyDenied : PermissionStatus
}
