package com.kiero.presentation.parent.screen.mypage.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.parent.ParentInfo

@Composable
fun ParentMyPageUserInfo(
    info: ParentInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = info.profileImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_parent_profile),
            error = painterResource(R.drawable.ic_parent_profile),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(9.dp))

        Text(
            text = info.name,
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.white,
        )
    }
}

@Preview
@Composable
private fun ParentMyPageUserInfoPreview() {
    KieroTheme {
        ParentMyPageUserInfo(
            info = ParentInfo(
                name = "키어로"
            )
        )
    }
}
