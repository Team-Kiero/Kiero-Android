package com.kiero.presentation.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentTopbar(
    title: String,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = KieroTheme.colors.black,
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
                color = backgroundColor
            )
            .padding(top = 24.dp, bottom = 9.dp)
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (title.isEmpty()) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(23.dp)
                    .aspectRatio(108f / 23f)
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

@PreviewScreenSizes
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
