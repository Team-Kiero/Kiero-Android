package com.kiero.core.designsystem.theme

import androidx.compose.runtime.Immutable
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

sealed interface TypographyTokens {

    @Immutable
    data class Regular(
        val body1: TextStyle,
        val body2: TextStyle,
        val body3: TextStyle,
        val body4: TextStyle,
        val body5: TextStyle,
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
            letterSpacing = (-0.005).em,
            lineHeight = 1.3.em,
        ),
        body2 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 16.sp,
            letterSpacing = (-0.005).em,
            lineHeight = 1.3.em,
        ),
        body3 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 14.sp,
            letterSpacing = (-0.005).em,
            lineHeight = 1.3.em,
        ),
        body4 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 12.sp,
            letterSpacing = (-0.005).em,
            lineHeight = 1.3.em,
        ),
        body5 = TextStyle(
            fontFamily = NotoSansFont.regular,
            fontSize = 10.sp,
            letterSpacing = (-0.005).em,
            lineHeight = 1.3.em,
        ),
    ),
    semiBold = TypographyTokens.SemiBold(
        title1 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 22.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        ),
        title2 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 20.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        ),
        title3 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 16.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        ),
        title4 = TextStyle(
            fontFamily = NotoSansFont.semiBold,
            fontSize = 14.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        ),
    ),
    bold = TypographyTokens.Bold(
        headLine1 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 22.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        ),
        headLine2 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 20.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        ),
        headLine3 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 16.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        ),
        headLine4 = TextStyle(
            fontFamily = NotoSansFont.bold,
            fontSize = 14.sp,
            letterSpacing = (-0.01).em,
            lineHeight = 1.4.em,
        )
    )
)
