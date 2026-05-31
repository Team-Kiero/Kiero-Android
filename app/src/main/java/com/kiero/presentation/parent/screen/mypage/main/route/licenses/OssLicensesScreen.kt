package com.kiero.presentation.parent.screen.mypage.main.route.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors

@Composable
fun OssLicensesScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {
    val libraries by produceLibraries()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        KieroTopbar(
            title = "오픈소스 라이선스",
            leftIconClick = onBackClick,
            modifier = Modifier.padding(top = 30.dp)
        )
        LibrariesContainer(
            libraries = libraries,
            colors = LibraryDefaults.libraryColors(
                libraryContentColor = KieroTheme.colors.white,
                libraryBackgroundColor = KieroTheme.colors.black,
                dialogContentColor = KieroTheme.colors.white,
                dialogConfirmButtonColor = KieroTheme.colors.white
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun OssLicensesScreenPreview() {
    KieroTheme {
        OssLicensesScreen(
            paddingValues = PaddingValues(),
            onBackClick = {}
        )
    }
}