package com.kiero.presentation.parent.screen.mypage.childcare.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.text.KieroTextHolder
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentMyPageChildInfoModel

@Composable
fun ParentMyPageChildNameHolder(
    childInfo: ParentMyPageChildInfoModel,
    modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(49.dp))

        Text(
            text = "자녀 연결 관리",
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.gray400,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        KieroTextHolder(
            text = childInfo.fullName
        )

        Spacer(modifier = Modifier.height(6.dp))

        ParentMyPageChildStatus(
            isChildJoined = childInfo.isChildJoined,
            modifier = Modifier
                .align(Alignment.End)
        )
    }
}

@Preview
@Composable
private fun ParentMyPageChildNameHolderPreview() {
    KieroTheme {
        ParentMyPageChildNameHolder(
            childInfo = ParentMyPageChildInfoModel(
                childLastName = "키",
                childFirstName = "어로",
                isChildJoined = true
            )
        )
    }
}
