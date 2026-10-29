package com.smartlife.library_common.ext

import android.os.SystemClock
import android.view.View
import com.smartlife.library_common.R

// 默认防抖间隔时间：800毫秒
private const val CLICK_DEBOUNCE_INTERVAL = 800L

/**
 * View 防抖点击扩展方法
 * @param interval 两次点击的最小间隔时间，默认800ms
 * @param action 点击事件的具体执行逻辑
 */
fun View.clickDebounce(
    interval: Long = CLICK_DEBOUNCE_INTERVAL,
    action: (view: View) -> Unit
) {
    this.setOnClickListener { view ->
        val currentClickTime = SystemClock.elapsedRealtime()
        val lastClickTime = view.getTag(R.id.click_time_tag) as? Long ?: 0L

        // 如果距离上次点击时间超过了设定的间隔，则认为是有效点击
        if (currentClickTime - lastClickTime > interval) {
            // 更新最后一次点击的时间戳
            view.setTag(R.id.click_time_tag, currentClickTime)
            action.invoke(view)
        } else {
            // 处于防抖期，忽略本次点击
        }
    }
}