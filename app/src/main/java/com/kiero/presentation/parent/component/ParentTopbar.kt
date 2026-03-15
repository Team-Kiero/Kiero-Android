package com.kiero.presentation.parent.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ParentTopbar(
    title: String,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAlarmActive: Boolean = false,
) {
    val alarmRes = when (isAlarmActive) {
        true -> R.drawable.ic_parent_alarm_activate
        false -> R.drawable.ic_parent_alarm_default
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.black
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (title.isEmpty()) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.aspectRatio(108f / 23f)
            )
        } else {
            Text(
                text = title,
                color = KieroTheme.colors.white,
                style = KieroTheme.typography.bold.headLine2
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(id = alarmRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onAlarmClick)
        )
    }
}

@Preview
@Composable
private fun ParentTopbarPreview() {
    KieroTheme {
        ParentTopbar(
            title = "",
            onAlarmClick = {},
            isAlarmActive = true
        )
    }
}
