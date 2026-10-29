plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)          // Room / Hilt 注解处理需要
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.smartlife.feature_user"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":libraries:library_common"))
    implementation(project(":libraries:library_network"))

    // Hilt（插件要求必须在本模块显式声明）
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // 👇 加上这一行 Room 编译器
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}