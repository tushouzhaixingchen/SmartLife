package com.smartlife.library_common.utils

import android.util.Log

/**
 * 统一日志工具类
 * 可通过 TAG 开关控制日志输出，发布时一键关闭所有日志
 */
object LogUtils {

    /**
     * 全局日志开关
     * 发布正式版时设为 false，即可关闭所有日志输出
     */
    var enable: Boolean = true

    private const val DEFAULT_TAG = "SmartLife"

    fun v(msg: String, tag: String = DEFAULT_TAG) {
        if (enable) Log.v(tag, msg)
    }

    fun d(msg: String, tag: String = DEFAULT_TAG) {
        if (enable) Log.d(tag, msg)
    }

    fun i(msg: String, tag: String = DEFAULT_TAG) {
        if (enable) Log.i(tag, msg)
    }

    fun w(msg: String, tag: String = DEFAULT_TAG) {
        if (enable) Log.w(tag, msg)
    }

    fun e(msg: String, tag: String = DEFAULT_TAG, throwable: Throwable? = null) {
        if (enable) {
            if (throwable != null) {
                Log.e(tag, msg, throwable)
            } else {
                Log.e(tag, msg)
            }
        }
    }
}