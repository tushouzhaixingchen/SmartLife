plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.smartlife.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.smartlife.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // ===== 基础库（所有公共依赖通过它传递过来）=====
    implementation(project(":libraries:library_common"))
    implementation(project(":libraries:library_network"))

    // ===== 业务模块（后续创建后逐个取消注释）=====
    implementation(project(":features:feature_ai"))
    implementation(project(":features:feature_background"))
    implementation(project(":features:feature_charts"))
    implementation(project(":features:feature_home"))
    implementation(project(":features:feature_ipc"))
    implementation(project(":features:feature_music"))
    implementation(project(":features:feature_notes"))
    implementation(project(":features:feature_responsive"))
    implementation(project(":features:feature_user"))
    implementation(project(":features:feature_weather"))
    // ===== Shell =====
    implementation(project(":shell:shell_bottom"))
    implementation(project(":shell:shell_launcher"))
    // ===== Hilt（插件要求必须在本模块显式声明）=====
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // ===== 测试 =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}