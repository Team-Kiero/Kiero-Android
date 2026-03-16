package com.kiero.presentation.parent.screen.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.viewtype.DisplayType

@Composable
fun ParentRewardCard(
    name: String,
    price: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = KieroTheme.colors.gray900)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 35.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = name,
                style = KieroTheme.typography.regular.body3,
                color = KieroTheme.colors.white,
            )

            KieroChip(
                isEnabled = false,
                isCompleted = true,
                action = KieroCoinAction(
                    coinCount = price,
                    isCompleted = true,
                    isEnabled = false,
                    viewType = DisplayType.KID,
                    onClick = {}
                )
            )
        }
    }
}

@Preview
@Composable
private fun ParentRewardCardPreview() {
    KieroTheme {
        ParentRewardCard(
            name = "용돈 5000원 받기",
            price = 350,
            onClick = {}
        )
    }
}