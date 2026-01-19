package com.kiero.presentation.kid.journey.model

import androidx.annotation.DrawableRes
import com.kiero.R

enum class StoneUiType(
    val serverKey: String,
    val text: String,
    @param: DrawableRes val imageRes: Int,
) {
    COURAGE(
        serverKey = "COURAGE",
        text = "용기의 불조각",
        imageRes = R.drawable.img_kid_journey_stone_blue
    ),
    GRIT(
        serverKey = "GRIT",
        text = "인내의 불조각",
        imageRes = R.drawable.img_kid_journey_stone_red
    ),WISDOM(
        serverKey = "WISDOM",
        text = "지혜의 불조각",
        imageRes = R.drawable.img_kid_journey_stone_green
    );

    companion object {
        fun from(key: String): StoneUiType =
            entries.find { it.serverKey == key } ?: COURAGE
    }
}