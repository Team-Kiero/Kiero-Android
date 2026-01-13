package com.kiero.presentation.kid.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidProfileChip(
    kidName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 5.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_kid_profile),
            contentDescription = null,
        )

        Text(
            text = kidName,
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.white
        )
    }
}

@Preview
@Composable
private fun KidProfileChipPreview() {
    KieroTheme {
        KidProfileChip(
            kidName = "주완"
        )
    }
}
