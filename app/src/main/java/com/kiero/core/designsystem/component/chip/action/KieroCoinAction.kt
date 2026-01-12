package com.kiero.core.designsystem.component.chip.action

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.viewtype.DisplayType

/**
 * @isCompleted - 완료 여부를 제어합니다 (main 색상)
 * @isEnabled - 사용 가능 여부를 제어합니다 (회색, 흰색)
 * @viewType - 뷰타입을 통해 텍스트와 텍스트 스타일을 제어합니다
 * */
class KieroCoinAction(
    private val coinCount: Int,
    private val isCompleted: Boolean = false,
    private val isEnabled: Boolean = true,
    private val viewType: DisplayType = DisplayType.KID,
    private val onClick: () -> Unit
) : ChipAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        val coin = painterResource(id = R.drawable.img_kid_coin)

        val targetColor = when {
            !isEnabled -> KieroTheme.colors.gray500
            isCompleted -> KieroTheme.colors.main
            else -> KieroTheme.colors.gray100
        }

        val textContent = when (viewType) {
            DisplayType.KID -> "$coinCount 개"
            DisplayType.PARENT -> "$coinCount 개 사용"
        }

        val textStyle = when (viewType) {
            DisplayType.KID -> KieroTheme.typography.regular.body3
            DisplayType.PARENT -> KieroTheme.typography.regular.body5
        }

        Row(
            modifier = modifier
                .noRippleClickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = coin,
                contentDescription = null,
                modifier = Modifier
                    .forcePixelToDp(coin)
            )

            Text(
                text = textContent,
                style = textStyle,
                color = targetColor
            )
        }
    }
}

@Preview
@Composable
private fun KieroCoinActionPreview() {
    KieroTheme {
        KieroCoinAction(
            coinCount = 150,
            onClick = {},
            viewType = DisplayType.PARENT
        ).invoke(modifier = Modifier)
    }
}