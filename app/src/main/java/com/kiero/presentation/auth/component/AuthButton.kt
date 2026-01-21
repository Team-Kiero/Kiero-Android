package com.kiero.presentation.auth.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun AuthButton(
    @DrawableRes icon: Int,
    text : String,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    val painter = painterResource(id = icon)
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = KieroTheme.colors.white),
                onClick = onClickButton
            )
            .padding(vertical = 16.dp, horizontal = 29.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .forcePixelToDp(painter)
        )

        Text(
            text = text,
            style = KieroTheme.typography.regular.body2,
            color = KieroTheme.colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun AuthButtonPreview() {
    KieroTheme {
        AuthButton(
            text = "부모님으로 시작하기",
            icon = com.kiero.R.drawable.img_auth_parent_goblin,
            onClickButton = {}
        )
    }
}