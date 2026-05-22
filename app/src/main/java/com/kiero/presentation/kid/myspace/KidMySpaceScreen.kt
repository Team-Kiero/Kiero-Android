package com.kiero.presentation.kid.myspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.myspace.component.KidMySpaceNotification
import com.kiero.presentation.kid.myspace.component.KidMySpaceSettingItem
import com.kiero.presentation.kid.myspace.component.KidMySpaceWishArchive

@Composable
fun KidMySpaceRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit
) {
    KidMySpaceScreen(
        paddingValues = paddingValues
    )
}


@Composable
private fun KidMySpaceScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        KidProfileChip(
            kidName = "근영의 공간",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        KidMySpaceWishArchive(
            onClick = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            thickness = 2.dp,
            color = KieroTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.height(17.dp))

        KidMySpaceNotification(
            isChecked = false,
            onCheckedChange = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        KidMySpaceSettingItem(
            text = "키어로 이용 약속",
            onClick = {},
        )

        KidMySpaceSettingItem(
            text = "키어로 나가기",
            onClick = {}
        )

        Text(
            text = "앱 버전 v1.0.0",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray500,
            modifier = Modifier.padding(top = 17.dp, start = 9.dp)
        )
    }
}

@Preview
@Composable
private fun KidMySpaceScreenPreview() {
    KieroTheme {
        KidMySpaceScreen(
            paddingValues = PaddingValues()
        )
    }
}