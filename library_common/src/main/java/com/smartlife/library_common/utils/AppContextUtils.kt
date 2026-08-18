package com.smartlife.library_common.utils

import android.app.Application
import android.content.Context

/**
 * 全局 Context 持有工具
 * 必须在 Application.onCreate() 中调用 init() 初始化
 */
object AppContextUtils {

    private var application: Application? = null

    /**
     * 初始化，在 Application.onCreate() 中调用
     */
    fun init(app: Application) {
        application = app
    }

    /**
     * 获取全局 Application Context
     */
    fun getContext(): Context {
        return application
            ?: throw IllegalStateException("AppContextUtils 未初始化，请在 Application.onCreate() 中调用 init()")
    }

    /**
     * 获取 Application 实例
     */
    fun getApplication(): Application {
        return application
            ?: throw IllegalStateException("AppContextUtils 未初始化，请在 Application.onCreate() 中调用 init()")
    }
}