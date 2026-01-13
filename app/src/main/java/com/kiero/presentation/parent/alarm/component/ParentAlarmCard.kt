package com.kiero.presentation.parent.alarm.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.kiero.core.common.extension.toHighlightedText
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.viewtype.DisplayType

@Composable
fun ParentAlarmCard(
    time: String,
    message: String,
    highlightTexts: List<String>,
    highlightColor: Color,
    coinUsed: Int?,
    imageUrl: Any?,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasImage = imageUrl != null

    Card(
        modifier = modifier
            .widthIn(max = 343.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 68.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = KieroTheme.colors.gray900)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = time,
                    style = KieroTheme.typography.regular.body4,
                    color = KieroTheme.colors.gray500
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (hasImage) Modifier.noRippleClickable(onClick = onExpandClick)
                            else Modifier
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.toHighlightedText(
                            highlightColor, *highlightTexts.toTypedArray()
                        ),
                        style = KieroTheme.typography.regular.body2,
                        color = KieroTheme.colors.white,
                        modifier = Modifier.weight(1f)
                    )

                    if (hasImage) {
                        Icon(
                            imageVector = if (isExpanded) ImageVector.vectorResource(id = R.drawable.ic_arrow_up)
                            else ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
                            contentDescription = "확장 버튼",
                            tint = KieroTheme.colors.white
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))


            // 3. 코인 정보 (사진이 없을 때만, 왼쪽 정렬)
            if (!hasImage && coinUsed != null) {
                KieroChip(
                    isEnabled = true, isCompleted = false, action = KieroCoinAction(
                    coinCount = coinUsed,
                    isCompleted = false,
                    isEnabled = true,
                    viewType = DisplayType.PARENT,
                    onClick = { }))
            }

            // 4. 확장 이미지
            if (hasImage && isExpanded) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview
@Composable
private fun ParentAlarmCardPreview() {
    KieroTheme {
        var expandedState by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 케이스 1: 이미지가 있는 경우 (토글)
            ParentAlarmCard(
                time = "12 : 00",
                message = "근영이가 피아노 학원에 도착했어요.",
                highlightTexts = listOf("피아노 학원"),
                highlightColor = KieroTheme.colors.main,
                coinUsed = 150,
                imageUrl = R.drawable.img_kid_journey_piano_background,
                isExpanded = expandedState,
                onExpandClick = { expandedState = !expandedState })

            // 케이스 2: 이미지가 없는 경우 (코인 칩)
            ParentAlarmCard(
                time = "오늘 14 : 30",
                message = "근영이가 게임 30분 추가 쿠폰을 사용했어요",
                highlightTexts = listOf("게임 30분 추가"),
                highlightColor = KieroTheme.colors.main,
                coinUsed = 100,
                imageUrl = null,
                isExpanded = false,
                onExpandClick = {})
        }
    }
}