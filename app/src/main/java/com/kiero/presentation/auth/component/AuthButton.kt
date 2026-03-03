package com.kiero.presentation.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun AuthButton(
    text : String,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = KieroTheme.colors.white),
                onClick = onClickButton
            )
            .padding(vertical = 29.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_addschedule_check_off),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 10.dp)
        )

        Text(
            text = text,
            style = KieroTheme.typography.regular.body2,
            color = KieroTheme.colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 34.dp)
        )
    }
}

@Preview
@Composable
private fun AuthButtonPreview() {
    KieroTheme {
        AuthButton(
            text = "부모님으로",
            onClickButton = {}
        )
    }
}