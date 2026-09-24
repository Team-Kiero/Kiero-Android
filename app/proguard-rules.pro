# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 카카오 SDK의 모델 클래스는 JSON 변환에 사용되므로 난독화하지 않음
# (com.kakao.sdk 2.20.6은 model 패키지에 대한 consumer rule을 제공하지 않아 직접 유지 필요.
#  앱에서 실제로 쓰는 auth/common 패키지로 범위를 좁힘)
-keep class com.kakao.sdk.auth.model.* { <fields>; }
-keep class com.kakao.sdk.common.model.* { <fields>; }

# OkHttp 관련 선택적 보안 라이브러리 경고 무시
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

# Retrofit2, 코루틴 Continuation 관련 rule은 Retrofit 3.0.0의
# META-INF/proguard/retrofit2.pro에 동일하게 번들되어 있어 제거함