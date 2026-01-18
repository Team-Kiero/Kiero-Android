package com.kiero.presentation.signup.parent.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentSignUpInviteHolder(
    code: String,
    expiredTime: String,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 42.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = KieroTheme.colors.gray100,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "우리 아이 초대 코드",
                style = KieroTheme.typography.regular.body4,
                color = KieroTheme.colors.gray800,
            )

            Text(
                text = code,
                style = KieroTheme.typography.bold.headLine1,
                color = KieroTheme.colors.black,
            )
        }

        Spacer(modifier = Modifier.height(11.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = KieroTheme.colors.black,
                    shape = RoundedCornerShape(8.dp)
                )
                .noRippleClickable(
                    onClick = onCopyClick
                )
                .padding(vertical = 10.dp, horizontal = 37.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_copy),
                contentDescription = null,
                tint = Color.Unspecified
            )
            
            Text(
                text = "복사하기",
                style = KieroTheme.typography.semiBold.title3,
                color = KieroTheme.colors.white,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(19.dp))

        Text(
            text = "유효기간 $expiredTime",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.schedule1,
        )

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = "이 코드를 아이에게 알려주시고, \n" +
                    "회원가입 시 입력하도록 안내해주세요.",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray200,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun ParentSignUpInviteHolderPreview() {
    KieroTheme {
        ParentSignUpInviteHolder(
            code = "213123",
            expiredTime = "10:00",
            onCopyClick = {}
        )
    }
}