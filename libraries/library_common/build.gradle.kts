plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.smartlife.library_common"
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
        viewBinding = true  // 启用 ViewBinding，用来替代 findViewById 获取控件
    }
}

dependencies {
    // ===== AndroidX 基础 =====
    api(libs.androidx.core.ktx)              // Android 核心扩展库，提供协程、生命周期等 Kotlin 扩展
    api(libs.androidx.appcompat)             // 兼容库，让旧版本 Android 也能使用新特性（如 Theme、ActionBar）
    api(libs.material)                       // Material Design 组件库，提供按钮、卡片、输入框等标准 UI 控件
    api(libs.androidx.constraintlayout)      // 约束布局，用约束关系定位控件，减少布局嵌套层级
    api(libs.androidx.activity)              // Activity 基类，提供对生命周期、权限请求等的支持
    api(libs.androidx.fragment.ktx)          // Fragment 扩展库，提供协程支持等 Kotlin 便捷写法

    // ===== 生命周期 =====
    api(libs.androidx.lifecycle.runtime.ktx) // 生命周期感知组件，让代码自动响应 Activity/Fragment 的创建、销毁等事件
    api(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel 组件，页面旋转时保留数据不丢失，配合协程使用

    // ===== 依赖注入 =====
    api(libs.hilt.android)                   // 依赖注入框架，自动创建和管理对象，不用手动 new
    kapt(libs.hilt.compiler)                 // Hilt 的编译器，编译时自动生成依赖注入的代码

    // ===== 数据库 =====
    api(libs.room.runtime)                   // 本地数据库框架，SQLite 的官方封装，用 Kotlin 代码操作数据库
    api(libs.room.ktx)                       // Room 的协程扩展，支持 suspend 函数和 Flow 查询
    kapt(libs.room.compiler)                 // Room 的编译器，编译时自动生成数据库操作的实现代码

    // ===== 协程 =====
    api(libs.kotlinx.coroutines.core)        // 协程核心库，用同步写法写异步代码，替代 Thread 和 Handler
    api(libs.kotlinx.coroutines.android)     // 协程的 Android 扩展，提供 Main 调度器，让协程在主线程更新 UI

    // ==== gson ====
    api(libs.gson)

    // ==== arouter ====
    api(libs.arouter.api)

    // ===== 数据存储datastore =====
    api(libs.androidx.datastore)

    // ===== 测试 =====
    testImplementation(libs.junit)           // 单元测试框架，测试纯逻辑代码（不依赖 Android 环境）
    androidTestImplementation(libs.androidx.junit)           // Android 仪器测试框架，在真机或模拟器上运行测试
    androidTestImplementation(libs.androidx.espresso.core)   // UI 测试框架，模拟用户点击、滑动等操作来测试界面
}