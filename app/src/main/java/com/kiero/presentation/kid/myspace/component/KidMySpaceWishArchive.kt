package com.kiero.presentation.kid.myspace.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMySpaceWishArchive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = KieroTheme.colors.gray700,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .padding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_kid_well_of_wish),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(18.dp)
                )

                Spacer(modifier = Modifier.width(7.dp))

                Text(
                    text = "소원의 공간",
                    style = KieroTheme.typography.semiBold.title3,
                    color = KieroTheme.colors.white
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "내가 빌었던 소원들을 모아볼 수 있어!",
                style = KieroTheme.typography.regular.body4,
                color = KieroTheme.colors.gray400
            )
        }

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = KieroTheme.colors.white,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Preview
@Composable
private fun KidMySpaceWishArchivePreview() {
    KieroTheme {
        KidMySpaceWishArchive(
            onClick = {}
        )
    }
}