package com.kiero.presentation.parent.alarm.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = KieroTheme.colors.gray900)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
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
                        style = KieroTheme.typography.semiBold.title3,
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

            if (!hasImage && coinUsed != null) {
                Spacer(modifier = Modifier.height(6.dp))
                KieroChip(
                    modifier = Modifier.align(Alignment.Start),
                    isEnabled = true, isCompleted = false, action = KieroCoinAction(
                        coinCount = coinUsed,
                        isCompleted = false,
                        isEnabled = true,
                        viewType = DisplayType.PARENT,
                        onClick = { })
                )
            }

            if (hasImage && isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        //animateContentSize() 추가
                        .clip(RoundedCornerShape(8.dp))
                        .background(KieroTheme.colors.black)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}


    @Preview(showBackground = true, backgroundColor = 0xFF000000)
    @Composable
    private fun ParentAlarmCardPreview() {
        KieroTheme {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ParentAlarmCard(
                    time = "12 : 00",
                    message = "근영이가 과자먹기에 도착했어요.",
                    highlightTexts = listOf("과자먹기"),
                    highlightColor = Color(0xFF00FFE1),
                    coinUsed = null,
                    imageUrl = R.drawable.img_kid_journey_piano_background,
                    isExpanded = true, // ✅ 확장 상태 고정
                    onExpandClick = {}
                )

                ParentAlarmCard(
                    time = "14 : 30",
                    message = "미션 '미션 1'을 완료했어요.",
                    highlightTexts = listOf("미션 1"),
                    highlightColor = Color(0xFF00FFE1),
                    coinUsed = 100,
                    imageUrl = null,
                    isExpanded = false,
                    onExpandClick = {}
                )
            }
        }
    }