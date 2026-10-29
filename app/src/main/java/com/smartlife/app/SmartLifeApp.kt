package com.smartlife.app

import android.app.Application
import com.smartlife.library_common.auth.LoginSession
import com.smartlife.library_common.utils.AppContextUtils
import com.smartlife.library_common.utils.DataStoreUtils
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
        DataStoreUtils.init(this)   // 全局唯一初始化点
        LoginSession.restore()// 2. 从磁盘读取登录态到内存（启动阶段一次，之后所有判断都走内存）
        AppContextUtils.init(this)
    }
}