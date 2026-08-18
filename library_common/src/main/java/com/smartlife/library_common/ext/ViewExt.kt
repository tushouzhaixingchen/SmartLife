package com.smartlife.library_common.ext

import android.view.View

/**
 * View 扩展函数
 */

/**
 * 设置防抖点击事件
 * 防止用户短时间内多次点击触发重复操作
 * @param interval 点击间隔时间（毫秒），默认 500ms
 * @param onClick 点击回调
 */
fun View.setOnDebounceClickListener(
    interval: Long = 500L,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= interval) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

/**
 * 显示 View
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * 隐藏 View（占位但不可见）
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 隐藏 View（不占位）
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 根据条件切换 View 的显示/隐藏
 * @param isVisible true 显示，false 隐藏（gone）
 */
fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * 根据条件切换 View 的显示/不可见
 * @param isVisible true 显示，false 不可见（invisible）
 */
fun View.setVisibleOrInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}