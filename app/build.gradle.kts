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
    implementation(project(":library_common"))
    implementation(project(":library_network"))

    // ===== 业务模块（后续创建后逐个取消注释）=====
    // implementation(project(":module-adaptation"))
    // implementation(project(":module-chart"))
    // implementation(project(":module-ipc"))
    // implementation(project(":module-music"))
    // implementation(project(":module-note"))
    // implementation(project(":module-task"))
    // implementation(project(":module-user"))
    // implementation(project(":module-weather"))
    // ===== Hilt（插件要求必须在本模块显式声明）=====
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // ===== 测试 =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}