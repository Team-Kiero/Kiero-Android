package com.kiero.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val localKieroColors = staticCompositionLocalOf { defaultKieroColors }

val localKieroTypography = staticCompositionLocalOf { defaultKieroTypography }

private val LightColorScheme = lightColorScheme(
    primary = Black,
    background = White,
    surface = White,
    onPrimary = White,
    onBackground = Black,
    onSurface = Black
)

object KieroTheme {
    val colors: KieroColors
        @Composable
        @ReadOnlyComposable
        get() = localKieroColors.current

    val typography: KieroTypography
        @Composable
        @ReadOnlyComposable
        get() = localKieroTypography.current
}

@Composable
fun ProvideKieroColorsAndTypography(
    colors: KieroColors,
    typography: KieroTypography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        localKieroColors provides colors,
        localKieroTypography provides typography,
        content = content
    )
}

@Composable
fun KieroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    ProvideKieroColorsAndTypography(
        colors = defaultKieroColors,
        typography = defaultKieroTypography
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            content = content
        )
    }
}