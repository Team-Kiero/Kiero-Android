package com.kiero.presentation.kid.onboarding

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.Color
import com.kiero.R


enum class OnboardingUiModel (
    val step : Int,
    val backImage : Int,
    val description : String
){
    STORY1(1, R.drawable.img_kid_onboarding_0,"Story 1"),
    STORY2(2,R.drawable.img_kid_onboarding_1,"Story 2"),
    STORY3(3, R.drawable.img_kid_onboarding_2,"Story 3"),
    STORY4(4,R.drawable.img_kid_onboarding_3,"Story 4"),
    STORY5(5,R.drawable.img_kid_onboarding_4,"Story 5")
}

@Immutable
data class DescriptionModel(
    val description : AnnotatedString
){
    companion object{
        fun getDescription(step : Int, mainColor: Color) : DescriptionModel{
            return when(step){
                1 -> DescriptionModel(buildAnnotatedString {
                    append("드디어 만났다! 나의 짝궁 근영\n난 꼬마 히어로 꾸비야. 우리 같이 모험을 떠나볼까?")
                })
                2 -> DescriptionModel(buildAnnotatedString {
                    append("다른 도깨비들은 장난치는 걸 좋아하지만,\n난 '영웅의 불씨'를 품고 태어난 특별한 도꺠비야!\n너의 노력을 멋진 소원으로 바꾸는 ")
                    withStyle(style = SpanStyle(color = mainColor)) {
                        append("꼬마 히어로")
                    }
                    append(" 지.")
                })
                3 -> DescriptionModel(buildAnnotatedString {
                    append("그런데 큰일이야...\n배에 있는 '영웅의 불씨'가 자꾸 꺼지려고 해.\n나 혼자서는 지킬 수 없거든\n")
                    withStyle(style = SpanStyle(color = mainColor)) {
                        append("오직 너만이 이 불씨를 다시 키울수 있어!")
                    }
                })
                4 -> DescriptionModel(buildAnnotatedString {
                    append("오늘의 여정을 따라 하루를 보내고\n불조각을 나에게 건네줘!\n너가 준 ")
                    withStyle(style = SpanStyle(color = mainColor)) {
                        append("[용기,인내,지혜의 불조각]")
                    }
                    append(" 이\n내 마음이 불꽃을 키워줄거야")
                })
                5 -> DescriptionModel(buildAnnotatedString {
                    append("그 힘으로 내가 반짝이는 금화를 만들어줄게!\n소원의 우물에서 금화를 통해\n너의 소원을 이룰 수 있을거야!")
                })
                else -> DescriptionModel(buildAnnotatedString {
                    append("Story 1")
                })
            }
        }
    }
}