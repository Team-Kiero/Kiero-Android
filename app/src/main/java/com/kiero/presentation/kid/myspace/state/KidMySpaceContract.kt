package com.kiero.presentation.kid.myspace.state

import androidx.compose.runtime.Immutable
import com.kiero.core.common.extension.toWishArchiveDateString
import com.kiero.data.kid.wish.model.WishHistoryModel
import com.kiero.presentation.kid.myspace.wisharchive.model.KidMySpaceWishArchiveItemUiModel

@Immutable
data class KidMySpaceState(
    val kidName: String = "",
    val isNotificationChecked: Boolean = false,
    val showNotificationDialog: Boolean = false,
    val permissionNotificationDeniedCount: Int = 0,
)

fun WishHistoryModel.toUiModel(index : Int) : KidMySpaceWishArchiveItemUiModel {
    return KidMySpaceWishArchiveItemUiModel(
        id = index.toLong(),
        name = name,
        acquiredAt = purchasedAt.toWishArchiveDateString(),
        price = price
    )
}