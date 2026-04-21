package com.kiero.data.sse.model

data class RawSseEvent(
    val type: String?,
    val data: String,
    val id: String?,
)