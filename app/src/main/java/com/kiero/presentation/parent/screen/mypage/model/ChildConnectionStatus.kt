package com.kiero.presentation.parent.screen.mypage.model

enum class ChildConnectionStatus(val text: String) {
    PENDING("연결 대기"),          // hasPendingChildSession == true
    REISSUE("연결 코드 재발급"),       // hasPendingChildSession == false (아이 연결 완료 또는 초기 상태)
    CONNECTED("연결 완료") // 재발급 후 아이 연결완료 시
}
