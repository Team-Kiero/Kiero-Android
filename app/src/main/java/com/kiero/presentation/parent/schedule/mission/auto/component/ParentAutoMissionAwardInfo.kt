package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAutoMissionAwardInfo(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_kid_coin),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "보상",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.regular.body2
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun ParentAutoMissionAwardInfoPreview() {
    KieroTheme {
        ParentAutoMissionAwardInfo()
    }
}