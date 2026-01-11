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
-keep class com.kakao.sdk.**.model.* { <fields>; }

# OkHttp 관련 선택적 보안 라이브러리 경고 무시
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

# Retrofit2 (with r8 full mode)
# Retrofit API 인터페이스 보존 (어노테이션 기반이므로 필수)
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# 코루틴 Continuation 클래스 보존 (suspend 함수 지원용)
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Retrofit 메서드의 반환 타입 보존
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# Retrofit Response 클래스 보존
-keep,allowobfuscation,allowshrinking class retrofit2.Response