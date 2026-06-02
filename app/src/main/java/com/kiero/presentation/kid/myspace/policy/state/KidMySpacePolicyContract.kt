package com.kiero.presentation.kid.myspace.policy.state

import com.kiero.presentation.kid.myspace.policy.model.KidMySpaceMenuUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class KidMySpacePolicyState(
    val myPageMenus: ImmutableList<KidMySpaceMenuUiModel> = persistentListOf(),
)