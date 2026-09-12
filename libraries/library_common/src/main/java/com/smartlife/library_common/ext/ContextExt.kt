package com.smartlife.library_common.ext

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat

/**
 * Context 扩展函数
 */

/**
 * 快速跳转 Activity
 */
inline fun <reified T : Any> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.block()
    startActivity(intent)
}

/**
 * 快速显示 Toast
 */
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

/**
 * 获取颜色资源
 */
fun Context.colorOf(colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}

/**
 * dp 转 px
 */
fun Context.dpToPx(dp: Float): Int {
    return (dp * resources.displayMetrics.density + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun Context.pxToDp(px: Float): Int {
    return (px / resources.displayMetrics.density + 0.5f).toInt()
}