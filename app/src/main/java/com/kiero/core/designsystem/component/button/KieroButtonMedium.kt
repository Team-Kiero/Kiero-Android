package com.kiero.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiero.core.designsystem.component.button.model.KieroButtonColors
import com.kiero.core.designsystem.theme.defaultKieroColors


@Composable
fun KieroButtonMedium(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
    colors: KieroButtonColors = KieroButtonColors(),
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(328.dp)
            .height(46.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor(),
            contentColor = colors.contentColor()
        ),
        contentPadding = PaddingValues(vertical = 11.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    painter = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.contentColor()
                )

                Spacer(modifier = Modifier.width(10.dp))
            }

            Text(
                text = text,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun KieroButtonMediumPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp), // 버튼 사이 간격
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KieroButtonMedium(
            text = "시작하기",
            onClick = { }
        )

        KieroButtonMedium(
            text = "일정 추가하기",
            leadingIcon = painterResource(id = com.kiero.R.drawable.ic_kid_camera),
            colors = KieroButtonColors(
                containerColor = defaultKieroColors.gray900,
                contentColor = defaultKieroColors.white
            ),
            onClick = { }
        )
    }
}