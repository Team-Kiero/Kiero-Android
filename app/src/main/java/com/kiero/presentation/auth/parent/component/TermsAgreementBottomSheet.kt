package com.kiero.presentation.auth.parent.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.disableUpSheetScroll
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.bottomsheet.KieroBottomSheet
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.auth.parent.model.AuthParentConsentsUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAgreementBottomSheet(
    consentsItem: AuthParentConsentsUiModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    navigateToTerms: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier
) {
    KieroBottomSheet(
        onDismiss = onDismiss,
    ) {
        TermsAgreementContent(
            consentsItem = consentsItem,
            onConfirm = onConfirm,
            navigateToTerms = navigateToTerms,
            navigateToPrivacyPolicy = navigateToPrivacyPolicy,
            modifier = modifier
        )
    }
}

@Composable
private fun TermsAgreementContent(
    consentsItem: AuthParentConsentsUiModel,
    onConfirm: () -> Unit,
    navigateToTerms: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
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

        TermsItem(
            termsText = "(필수) KIERO 이용약관 동의",
            isSelected = consentsItem.isTermsAccepted,
            onClick = navigateToTerms,
        )

        Spacer(modifier = Modifier.height(24.dp))

        TermsItem(
            termsText = "(필수) 개인정보 필수 동의",
            isSelected = consentsItem.isPrivacyPolicyAccepted,
            onClick = navigateToPrivacyPolicy
        )

        Spacer(modifier = Modifier.height(20.dp))

        KieroButtonMedium(
            text = "확인",
            isEnabled = consentsItem.isTermsAccepted && consentsItem.isPrivacyPolicyAccepted,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isSelected) ImageVector.vectorResource(R.drawable.ic_parent_addschedule_check_on) else ImageVector.vectorResource(R.drawable.ic_permission_check_off),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onClick)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = termsText,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.white
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsAgreementBottomSheetPreview() {
    KieroTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "테스트"
            )

            TermsAgreementContent(
                consentsItem = AuthParentConsentsUiModel(),
                onConfirm = {},
                navigateToTerms = {},
                navigateToPrivacyPolicy = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
