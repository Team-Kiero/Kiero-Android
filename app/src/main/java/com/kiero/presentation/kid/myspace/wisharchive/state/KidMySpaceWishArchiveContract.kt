package com.kiero.presentation.kid.myspace.wisharchive.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.myspace.wisharchive.model.KidMySpaceWishArchiveItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidMySpaceWishArchiveState(
    val totalCount: Int = 0,
    val todayWishes: ImmutableList<KidMySpaceWishArchiveItemUiModel> = persistentListOf(),
    val previousWishes: ImmutableList<KidMySpaceWishArchiveItemUiModel> = persistentListOf(),
    val isLoading: Boolean = false
) {
    companion object {
        val FAKE = KidMySpaceWishArchiveState(
            totalCount = 8,
            todayWishes = persistentListOf(
                KidMySpaceWishArchiveItemUiModel(
                    id = 1L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 10일",
                    price = 130
                ),
                KidMySpaceWishArchiveItemUiModel(
                    id = 2L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 10일",
                    price = 180
                )
            ),
            previousWishes = persistentListOf(
                KidMySpaceWishArchiveItemUiModel(
                    id = 3L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 15일",
                    price = 100
                ),
                KidMySpaceWishArchiveItemUiModel(
                    id = 4L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 15일",
                    price = 100
                ),
                KidMySpaceWishArchiveItemUiModel(
                    id = 5L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 15일",
                    price = 100
                ),
                KidMySpaceWishArchiveItemUiModel(
                    id = 6L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 10일",
                    price = 100
                ),
                KidMySpaceWishArchiveItemUiModel(
                    id = 7L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 15일",
                    price = 100
                ),
                KidMySpaceWishArchiveItemUiModel(
                    id = 8L,
                    name = "상헌 아트디렉터되기",
                    acquiredAt = "5월 10일",
                    price = 100
                )
            )
        )

        val FAKE_EMPTY = KidMySpaceWishArchiveState(
            totalCount = 0,
            todayWishes = persistentListOf(),
            previousWishes = persistentListOf()
        )
    }
}