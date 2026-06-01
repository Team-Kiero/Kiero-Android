package com.kiero.presentation.kid.myspace.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.kid.myspace.component.KidMySpaceSettingItem
import com.kiero.presentation.kid.myspace.policy.model.KidMenuLinkType
import com.kiero.presentation.kid.myspace.policy.viewmodel.KidMySpacePolicyViewModel

@Composable
fun KidMySpacePolicyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToOssLicenses: () -> Unit,
    viewModel: KidMySpacePolicyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val globalTrigger = LocalGlobalUiEventTrigger.current

    KidMySpacePolicyScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        navigateToOssLicenses = navigateToOssLicenses,
        onClickTerms = { type ->
            val link = state.myPageMenus.find { it.linkType == type }?.link
            if (!link.isNullOrEmpty()) {
                uriHandler.openUri(link)
            } else {
                globalTrigger.showToast("링크를 찾을 수 없습니다.")
            }
        }
    )
}

@Composable
private fun KidMySpacePolicyScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToOssLicenses: () -> Unit,
    onClickTerms: (KidMenuLinkType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        KieroTopbar(
            title = "키어로 이용 약속",
            leftIconRes = R.drawable.ic_arrow_left,
            leftIconClick = navigateUp,
        )

        Spacer(modifier = Modifier.height(17.dp))

        Text(
            text = "약관 및 정책",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray300,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        KidMySpaceSettingItem(
            text = "서비스 이용 약관",
            onClick = { onClickTerms(KidMenuLinkType.SERVICE_TERMS) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        KidMySpaceSettingItem(
            text = "개인정보 처리방침",
            onClick = { onClickTerms(KidMenuLinkType.PRIVACY_POLICY) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        KidMySpaceSettingItem(
            text = "오픈소스 라이선스",
            onClick = navigateToOssLicenses,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun KidMySpacePolicyScreenPreview() {
    KieroTheme {
        KidMySpacePolicyScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            navigateToOssLicenses = {},
            onClickTerms = {}
        )
    }
}