import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}


android {
    namespace = "com.kiero"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.Kiero"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Server URL to BuildConfig
        buildConfigField("String", "BASE_URL", properties["base.url"].toString())

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
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    flavorDimensions += "version"
    productFlavors {
        create("parent") {
            dimension = "version"
            applicationIdSuffix = ".parent"
            resValue("string", "app_name", "Kiero_Parent")
        }

        create("child") {
            dimension = "version"
            applicationIdSuffix = ".child"
            resValue("string", "app_name", "Kiero_Kid")
        }
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.test)

    debugImplementation(libs.bundles.debug)

    implementation(libs.bundles.androidx)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.kotlinx.immutable)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.bundles.coil)

    implementation(libs.timber)

    implementation(libs.kakao.user)


    implementation(libs.androidx.datastore.preferences)
    implementation(libs.tink.android)
}