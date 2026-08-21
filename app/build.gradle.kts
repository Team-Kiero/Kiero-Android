import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.aboutlibraries.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.kiero"
    compileSdk = libs.versions.compileSdk.get().toInt()

    signingConfigs {
        // Todo: local properties로 키 옮기기
        getByName("debug") {
            val debugKeystorePath = System.getProperty("user.home") + "/.android/debug.keystore"
            storeFile = file(debugKeystorePath)
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        create("benchmark") {
            storeFile = file(System.getProperty("user.home") + "/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    defaultConfig {
        applicationId = "com.Kiero"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "app_name", "KIERO")

        // KAKAO_NATIVE_APP_KEY
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"${properties["kakao.native.app.key"]}\"" // 명시적으로 따옴표 추가
        )

        // manifestPlaceholders for AndroidManifest
        manifestPlaceholders["NATIVE_APP_KEY"] = properties["kakao.native.app.key"].toString()

        // Todo : (Issue) LocalProperties의 "" 유무 및 일관성
        //buildConfigField("String", "KAKAO_NATIVE_KEY", properties["kakao.native.app.key"].toString())
        // manifestPlaceholders["NATIVE_APP_KEY"] = properties["kakao.native.app.key"].toString().replace("\"", "")

    }
    buildTypes {
        // Todo: 이거도 하나로 합치기 및 난독화 적용 후 테스트도 해보기

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Todo: isMinifyEnabled = true로 바꿀 때 추가
            // firebaseCrashlytics {
            //     mappingFileUploadEnabled = true
            // }
        }
    }
    composeCompiler {
        metricsDestination = layout.buildDirectory.dir("compose_metrics")
        reportsDestination = layout.buildDirectory.dir("compose_reports")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    // version: 보호자용(parent) / 아이용(child) 앱 구분 - applicationId, 아이콘, 런처명이 다름
    // environment: 접속 서버 구분 - BASE_URL, 앱 표시명(Dev 여부)이 다름 (buildType과 독립적인 축)
    //
    // buildType(debug/release) x environment(dev/prod) 조합별 용도:
    //   devDebug   - 평소 로컬 개발. 개발 서버 + 로깅/브레이크포인트/난독화 없음
    //   devRelease - 개발 서버 대상으로 release 빌드 설정(난독화 등) 검증
    //   prodDebug  - 실 운영 서버 + 디버그 도구 켜짐. 프로덕션에서만 재현되는 버그 추적, 배포 전 최종 QA
    //   prodRelease- 실제 스토어 배포 빌드
    flavorDimensions += listOf("version", "environment")
    productFlavors {
        create("parent") {
            dimension = "version"
            applicationIdSuffix = ".parent"
            resValue("string", "launcher_name", "kiero-parent")
        }

        create("child") {
            dimension = "version"
            applicationIdSuffix = ".child"
            resValue("string", "launcher_name", "kiero-child")
        }

        create("dev") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", properties["base.url.dev"].toString())
            resValue("string", "app_name", "KIERO (Dev)")
        }

        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", properties["base.url.prod"].toString())
        }
    }

    // Amplitude 키: prodRelease(실 스토어 배포)만 release 키, 나머지 조합(devDebug/devRelease/prodDebug)은 전부 debug 키
    @Suppress("DEPRECATION")
    applicationVariants.all {
        val isProdRelease = flavorName.endsWith("Prod") && buildType.name == "release"
        val amplitudeKey = properties[if (isProdRelease) "amplitude.api.key.release" else "amplitude.api.key.debug"]
        buildConfigField("String", "AMPLITUDE_API_KEY", "\"$amplitudeKey\"")
    }

    sourceSets {
        // debug 아이콘은 environment(dev/prod)와 무관하게 version(parent/child)별로만 다르므로
        // 기존 src/parentDebug, src/childDebug 폴더를 그대로 재사용
        listOf("parentDevDebug", "parentProdDebug").forEach {
            maybeCreate(it).res.srcDirs("src/parentDebug/res")
        }
        listOf("childDevDebug", "childProdDebug").forEach {
            maybeCreate(it).res.srcDirs("src/childDebug/res")
        }
    }

}

dependencies {
    implementation(libs.androidx.profileinstaller)
    testImplementation(libs.bundles.unitTest)
    debugImplementation(libs.leakcanary.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.test)
    "baselineProfile"(project(":baselineprofile"))

    debugImplementation(libs.bundles.debug)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx)

    implementation(libs.kotlinx.immutable)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.bundles.coil)
    implementation(libs.lottie.compose)

    implementation(libs.timber)

    implementation(libs.kakao.user)


    implementation(libs.androidx.datastore.preferences)
    implementation(libs.tink.android)

    implementation(libs.aboutlibraries.compose.m3)
    implementation(libs.wheelpicker)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.amplitude.analytics.android)
}
