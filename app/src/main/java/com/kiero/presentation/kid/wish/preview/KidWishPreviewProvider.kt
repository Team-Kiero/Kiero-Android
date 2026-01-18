package com.kiero.presentation.kid.wish.preview

import com.kiero.core.common.base.BaseUiStateProvider
import com.kiero.presentation.kid.wish.model.KidWishUiModel
import com.kiero.presentation.kid.wish.state.KidWishState
import kotlinx.collections.immutable.PersistentList

class KidWishPreviewProvider : BaseUiStateProvider<PersistentList<KidWishUiModel>>(
    successData = KidWishState.FAKE
)