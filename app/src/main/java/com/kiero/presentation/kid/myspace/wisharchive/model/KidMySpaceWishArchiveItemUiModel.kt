package com.kiero.presentation.kid.myspace.wisharchive.model

import androidx.compose.runtime.Immutable

@Immutable
data class KidMySpaceWishArchiveItemUiModel(
    val id: Long,
    val name: String,
    val acquiredAt: String,
    val price: Int
)