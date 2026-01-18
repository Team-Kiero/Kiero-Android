package com.kiero.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFD6D6D6)
val Gray300 = Color(0xFFC2C2C2)
val Gray400 = Color(0xFFADADAD)
val Gray500 = Color(0xFF999999)
val Gray600 = Color(0xFF848484)
val Gray700 = Color(0xFF707070)
val Gray800 = Color(0xFF5C5C5C)
val Gray900 = Color(0xFF2D2F34)
val Black = Color(0xFF232428)

val Main = Color(0xFF00FFE1)
val Schedule1 = Color(0xFFCFFFFA)
val Schedule2 = Color(0xFFFFFEE9)
val Schedule3 = Color(0xFFBFFFE3)
val Schedule4 = Color(0xFF34D9D3)
val Schedule5 = Color(0xFF7BBDFF)
val Point = Color(0xFFFF7BA2)

val Yellow = Color(0xFFFAE100)

@Immutable
data class KieroColors(
    val white: Color = White,
    val black: Color = Black,

    val main: Color = Main,
    val schedule1: Color = Schedule1,
    val schedule2: Color = Schedule2,
    val schedule3: Color = Schedule3,
    val schedule4: Color = Schedule4,
    val schedule5: Color = Schedule5,
    val point: Color = Point,

    val gray100: Color = Gray100,
    val gray200: Color = Gray200,
    val gray300: Color = Gray300,
    val gray400: Color = Gray400,
    val gray500: Color = Gray500,
    val gray600: Color = Gray600,
    val gray700: Color = Gray700,
    val gray800: Color = Gray800,
    val gray900: Color = Gray900,
    val yellow: Color = Yellow,
)

val defaultKieroColors = KieroColors()