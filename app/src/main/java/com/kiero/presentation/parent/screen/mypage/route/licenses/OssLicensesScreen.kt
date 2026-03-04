package com.kiero.presentation.parent.screen.mypage.route.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun OssLicensesScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {
    val libraries by produceLibraries()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.gray900)
            .padding(paddingValues)
    ) {
        KieroTopbar(
            title = "오픈소스 라이선스",
            leftIconClick = onBackClick
        )
        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier.fillMaxSize()
        )
    }
}
