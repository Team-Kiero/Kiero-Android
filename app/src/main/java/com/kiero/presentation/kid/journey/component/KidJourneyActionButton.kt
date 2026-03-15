package com.kiero.presentation.kid.journey.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.R
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.model.KidJourneyButtonType

@Composable
fun KidJourneyActionButton(
    buttonType: KidJourneyButtonType,
    buttonTextRes: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAuthButton = buttonType == KidJourneyButtonType.AUTH

    KieroButtonMedium(
        text = if (buttonTextRes != null) stringResource(buttonTextRes) else "",
        leadingIcon = if (isAuthButton) {
            ImageVector.vectorResource(id = R.drawable.ic_kid_camera)
        } else ImageVector.vectorResource(id = R.drawable.ic_fire),
        containerColor = KieroTheme.colors.gray100,
        contentColor = KieroTheme.colors.gray900,
        onClick = onClick,
        modifier = modifier.alpha(if (buttonType != KidJourneyButtonType.NONE) 1f else 0f)
    )
}

@Preview
@Composable
private fun KidJourneyActionButtonAuthPreview() {
    KieroTheme {
        KidJourneyActionButton(
            buttonType = KidJourneyButtonType.AUTH,
            buttonTextRes = R.string.journey_btn_auth,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun KidJourneyActionButtonFirePreview() {
    KieroTheme {
        KidJourneyActionButton(
            buttonType = KidJourneyButtonType.FIRE,
            buttonTextRes = R.string.journey_btn_fire,
            onClick = {}
        )
    }
}