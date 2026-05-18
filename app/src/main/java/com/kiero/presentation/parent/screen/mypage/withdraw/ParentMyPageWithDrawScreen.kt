package com.kiero.presentation.parent.screen.mypage.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme

// Todo: API 구현 시 반영
@Composable
fun ParentMyPageWithDrawScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
) {
    var isConfirmProcess by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        KieroTopbar(
            title = "회원 탈퇴",
            leftIconClick = onBackClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "보호자 계정이 탈퇴되면 연결된 자녀 계정에서도 서비스를 이용할 수 없습니다.\n" +
                    "\n" +
                    "삭제되는 정보\n" +
                    "  •   보호자 계정 정보\n" +
                    "  •   연결된 자녀 프로필 정보\n" +
                    "  •   자녀 일정 및 미션\n" +
                    "  •   금화/보상 정보\n" +
                    "  •   알림 설정 정보\n" +
                    "  •   서비스 이용 기록\n" +
                    "\n" +
                    "탈퇴 후에는 삭제된 데이터를 복구할 수 없습니다.\n" +
                    "같은 계정으로 다시 가입해도 기존 기록은 이어지지 않습니다.",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray200,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = KieroTheme.colors.gray900,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(horizontal = 20.dp, vertical = 28.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (isConfirmProcess) ImageVector.vectorResource(R.drawable.ic_parent_addschedule_check_on) else ImageVector.vectorResource(R.drawable.ic_parent_addschedule_check_off),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .noRippleClickable(onClick = {
                        isConfirmProcess = !isConfirmProcess
                    })
            )

            Text(
                text = "위 내용을 모두 확인했으며, 탈퇴 후 데이터가 복구되지 \n" +
                        "않는다는 점에 동의합니다.",
                style = KieroTheme.typography.regular.body3,
                color = KieroTheme.colors.white,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        KieroButtonMedium(
            text = "계정 삭제하고 탈퇴하기",
            isEnabled = isConfirmProcess,
            onClick = {
                if (isConfirmProcess) {
                    // TODO: 회원 탈퇴 API 호출
                    onBackClick()
                }
            }
        )
    }
}

@Preview
@Composable
private fun ParentMyPageWithDrawScreenPreview() {
    KieroTheme {
        ParentMyPageWithDrawScreen(
            paddingValues = PaddingValues(),
            onBackClick = {}
        )
    }
}
