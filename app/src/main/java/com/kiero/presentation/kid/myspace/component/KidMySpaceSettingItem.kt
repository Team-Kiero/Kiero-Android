package com.kiero.presentation.kid.myspace.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMySpaceSettingItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 16.dp)
            .noRippleClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.white,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun KidMySpaceSettingItemPreview() {
    KieroTheme {
        KidMySpaceSettingItem(
            text = "설정",
            onClick = {}
        )
    }
}
