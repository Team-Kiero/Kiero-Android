package com.kiero.presentation.signup.parent.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentSignUpTopBar(
    parentName: String,
    profileImage: String?,
    onClickProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row (
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = parentName,
            style = KieroTheme.typography.regular.body2,
            color = KieroTheme.colors.white
        )

        if (profileImage == null) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_parent_profile),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .noRippleClickable(onClick = onClickProfile)
            )
        } else {
            Spacer(modifier = Modifier.width(5.dp))

            AsyncImage(
                model = profileImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .noRippleClickable(onClick = onClickProfile)
            )
        }
    }
}

@Preview
@Composable
private fun ParentSignUpTopBarPreview() {
    KieroTheme {
        ParentSignUpTopBar(
            parentName = "김부모",
            profileImage = "http://k.kakaocdn.net/dn/cQudkk/btsEdCM24Re/xUGDYiK2MMcnTNnTdKzIt1/img_640x640.jpg",
            onClickProfile = {}
        )
    }
}