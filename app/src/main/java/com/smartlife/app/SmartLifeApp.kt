package com.smartlife.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用入口
 * @HiltAndroidApp 触发 Hilt 的代码生成，是依赖注入的起点
 */
@HiltAndroidApp
class SmartLifeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // 这里可以进行其他的全局初始化，例如 Timber、LeakCanary 等
        // 根据项目文档，更复杂的初始化可以交给 App Startup 库来管理
    }
}