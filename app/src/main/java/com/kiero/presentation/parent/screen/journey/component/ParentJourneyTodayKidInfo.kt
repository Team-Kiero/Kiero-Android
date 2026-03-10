package com.kiero.presentation.parent.screen.journey.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.util.toDotSeparatedDate
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.journey.ParentJourneyState
import com.kiero.presentation.parent.screen.journey.model.KidInfo

@Composable
fun ParentJourneyTodayKidInfo(
    kidInfo: KidInfo,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier.align(Alignment.Top),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = KieroTheme.typography.bold.headLine2.toSpanStyle()
                    ) {
                        append(kidInfo.kidName)
                    }
                    append("의 오늘의 현황")
                },
                style = KieroTheme.typography.bold.headLine3,
                color = KieroTheme.colors.white
            )

            Text(
                text = kidInfo.currentDate.toDotSeparatedDate(),
                style = KieroTheme.typography.regular.body4,
                color = KieroTheme.colors.gray500
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.img_auth_kid_goblin_small),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 10.dp)
                .aspectRatio(0.975f)
        )
    }
}

@Preview
@Composable
private fun ParentJourneyTodayKidInfoPreview() {
    KieroTheme {
        ParentJourneyTodayKidInfo(
            kidInfo = ParentJourneyState().kidInfo,
        )
    }
}
