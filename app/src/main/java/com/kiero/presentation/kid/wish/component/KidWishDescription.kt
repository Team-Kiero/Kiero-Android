package com.kiero.presentation.kid.wish.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidWishDescription(
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 49.dp, vertical = 27.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_kid_well_of_wish),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "소원의 우물",
            style = KieroTheme.typography.semiBold.title2,
            color = KieroTheme.colors.white
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "미션을 통해 얻은 금화로 소원을 살 수 있어!",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray300
        )
    }
}

@Preview
@Composable
private fun KidWishDescriptionPreview() {
    KieroTheme {
        KidWishDescription()
    }
}