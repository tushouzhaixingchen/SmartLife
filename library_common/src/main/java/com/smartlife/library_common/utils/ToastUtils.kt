package com.smartlife.library_common.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * 主线程安全的 Toast 工具类
 * 无论在哪个线程调用，都能正确显示
 */
object ToastUtils {

    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * 显示短 Toast
     */
    fun showShort(msg: String) {
        showToast(msg, Toast.LENGTH_SHORT)
    }

    /**
     * 显示长 Toast
     */
    fun showLong(msg: String) {
        showToast(msg, Toast.LENGTH_LONG)
    }

    private fun showToast(msg: String, duration: Int) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // 当前在主线程，直接显示
            Toast.makeText(AppContextUtils.getContext(), msg, duration).show()
        } else {
            // 当前在子线程，切回主线程显示
            mainHandler.post {
                Toast.makeText(AppContextUtils.getContext(), msg, duration).show()
            }
        }
    }
}