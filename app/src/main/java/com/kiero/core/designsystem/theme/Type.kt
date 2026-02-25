package com.kiero.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kiero.R

object NotoSansFont {
    val semiBold = FontFamily(Font(R.font.notosans_semibold))
    val bold = FontFamily(Font(R.font.notosans_bold))
    val regular = FontFamily(Font(R.font.notosans_regular))
}

private object TypographyDefaults {
    val RegularLetterSpacing = (-0.005).em
    val RegularLineHeight = 1.3.em

    val SemiBoldLetterSpacing = (-0.01).em
    val SemiBoldLineHeight = 1.4.em

    val BoldLetterSpacing = (-0.01).em
    val BoldLineHeight = 1.4.em

    val PlatformStyle = PlatformTextStyle(
        includeFontPadding = false
    )
}

sealed interface TypographyTokens {
    @Immutable
    data class Regular(
        val body1: TextStyle,
        val body2: TextStyle,
        val body3: TextStyle,
        val body4: TextStyle,
        val body5: TextStyle,
        val body6: TextStyle,
    )

    @Immutable
    data class SemiBold(
        val title1: TextStyle,
        val title2: TextStyle,
        val title3: TextStyle,
        val title4: TextStyle,
    )

    @Immutable
    data class Bold(
        val headLine1: TextStyle,
        val headLine2: TextStyle,
        val headLine3: TextStyle,
        val headLine4: TextStyle,
    )
}

@Immutable
data class KieroTypography(
    val regular: TypographyTokens.Regular,
    val semiBold: TypographyTokens.SemiBold,
    val bold: TypographyTokens.Bold,
)

val defaultKieroTypography = KieroTypography(
    regular = TypographyTokens.Regular(
        body1 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 18.sp,
            letterSpacing = TypographyDefaults.RegularLetterSpacing,
            lineHeight = TypographyDefaults.RegularLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        body2 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 16.sp,
            letterSpacing = TypographyDefaults.RegularLetterSpacing,
            lineHeight = TypographyDefaults.RegularLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        body3 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 14.sp,
            letterSpacing = TypographyDefaults.RegularLetterSpacing,
            lineHeight = TypographyDefaults.RegularLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        body4 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 12.sp,
            letterSpacing = TypographyDefaults.RegularLetterSpacing,
            lineHeight = TypographyDefaults.RegularLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        body5 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 11.sp,
            letterSpacing = TypographyDefaults.RegularLetterSpacing,
            lineHeight = TypographyDefaults.RegularLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        body6 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 10.sp,
            letterSpacing = TypographyDefaults.RegularLetterSpacing,
            lineHeight = TypographyDefaults.RegularLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
    ),
    semiBold = TypographyTokens.SemiBold(
        title1 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 22.sp,
            letterSpacing = TypographyDefaults.SemiBoldLetterSpacing,
            lineHeight = TypographyDefaults.SemiBoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        title2 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 20.sp,
            letterSpacing = TypographyDefaults.SemiBoldLetterSpacing,
            lineHeight = TypographyDefaults.SemiBoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        title3 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 16.sp,
            letterSpacing = TypographyDefaults.SemiBoldLetterSpacing,
            lineHeight = TypographyDefaults.SemiBoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        title4 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 14.sp,
            letterSpacing = TypographyDefaults.SemiBoldLetterSpacing,
            lineHeight = TypographyDefaults.SemiBoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
    ),
    bold = TypographyTokens.Bold(
        headLine1 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 22.sp,
            letterSpacing = TypographyDefaults.BoldLetterSpacing,
            lineHeight = TypographyDefaults.BoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        headLine2 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 20.sp,
            letterSpacing = TypographyDefaults.BoldLetterSpacing,
            lineHeight = TypographyDefaults.BoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        headLine3 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 16.sp,
            letterSpacing = TypographyDefaults.BoldLetterSpacing,
            lineHeight = TypographyDefaults.BoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        ),
        headLine4 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 14.sp,
            letterSpacing = TypographyDefaults.BoldLetterSpacing,
            lineHeight = TypographyDefaults.BoldLineHeight,
            platformStyle = TypographyDefaults.PlatformStyle
        )
    )
)
