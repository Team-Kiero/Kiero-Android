package com.kiero.domain.entity.sse

data class RawSseEvent(
    val type: String?,
    val data: String
)