package com.kiero.presentation.auth.parent.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.bottomsheet.KieroBottomSheet
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.auth.parent.model.TermsType
import com.kiero.presentation.auth.parent.model.TermsUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAgreementBottomSheet(
    termsList: ImmutableList<TermsUiModel>,
    isAllAgreed: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onClickTerms: (TermsType) -> Unit,
    navigateToTerms: (TermsType) -> Unit,
    modifier: Modifier = Modifier
) {
    KieroBottomSheet(
        onDismiss = onDismiss,
    ) {
        TermsAgreementContent(
            termsList = termsList,
            isAllAgreed = isAllAgreed,
            onConfirm = onConfirm,
            onClickTerms = onClickTerms,
            navigateToTerms = navigateToTerms,
            modifier = modifier
        )
    }
}

@Composable
private fun TermsAgreementContent(
    termsList: ImmutableList<TermsUiModel>,
    isAllAgreed: Boolean,
    onConfirm: () -> Unit,
    onClickTerms: (TermsType) -> Unit,
    navigateToTerms: (TermsType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                shadow = Shadow(
                    radius = 8.dp,
                    color = KieroTheme.colors.gray900,
                    offset = DpOffset(x = 0.dp, y = ((-3).dp))
                )
            )
            .background(
                color = KieroTheme.colors.black,
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
            )
    ) {
        Text(
            text = "아래 약관에 동의 후 서비스 이용이 가능해요.",
            style = KieroTheme.typography.bold.headLine3,
            color = KieroTheme.colors.white,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 23.dp, end = 16.dp, bottom = 11.dp)
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = KieroTheme.colors.gray800
        )

        Spacer(modifier = Modifier.height(24.dp))

        termsList.forEachIndexed { index, term ->
            TermsItem(
                termsText = term.termsType.description,
                isSelected = term.isAgreed,
                onClickTerms = { onClickTerms(term.termsType) },
                navigateToTerms = { navigateToTerms(term.termsType) },
            )

            if (index == termsList.lastIndex) {
                Spacer(modifier = Modifier.height(20.dp))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        KieroButtonMedium(
            text = "확인",
            isEnabled = isAllAgreed,
            onClick = onConfirm,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(37.dp))
    }
}

@Composable
private fun TermsItem(
    termsText: String,
    isSelected: Boolean,
    onClickTerms: () -> Unit,
    navigateToTerms: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            modifier = Modifier
                .weight(1f)
                .noRippleClickable(onClickTerms),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (isSelected) ImageVector.vectorResource(R.drawable.ic_parent_addschedule_check_on) else ImageVector.vectorResource(R.drawable.ic_permission_check_off),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Text(
                text = termsText,
                style = KieroTheme.typography.regular.body3,
                color = KieroTheme.colors.white,
            )
        }

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = navigateToTerms)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsAgreementBottomSheetPreview() {
    KieroTheme {
        val dummyTermsList = persistentListOf(
            TermsUiModel(
                termsType = TermsType.SERVICE_AGREEMENT,
                termsId = 1L,
                url = "",
                isAgreed = true
            ),
            TermsUiModel(
                termsType = TermsType.PRIVACY_POLICY,
                termsId = 2L,
                url = "",
                isAgreed = false
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "테스트"
            )

            TermsAgreementContent(
                termsList = dummyTermsList,
                isAllAgreed = false,
                onConfirm = {},
                navigateToTerms = {},
                onClickTerms = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
