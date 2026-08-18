package com.smartlife.library_common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView

/**
 * 全局通用的加载中 View
 * 包含一个居中的 ProgressBar + 文字提示
 *
 * 使用方式（在布局 XML 中）：
 * <com.smartlife.library_common.widget.LoadingView
 *     android:id="@+id/loadingView"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:visibility="gone" />
 *
 * 在代码中控制显示/隐藏：
 * binding.loadingView.showLoading("加载中...")
 * binding.loadingView.hideLoading()
 */
class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val progressBar: ProgressBar
    private val textView: TextView

    init {
        // 创建 ProgressBar
        progressBar = ProgressBar(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }

        // 创建文字提示
        textView = TextView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            ).apply {
                topMargin = 120 // 文字在进度条下方
            }
            textSize = 14f
            setTextColor(0xFF757575.toInt())
        }

        addView(progressBar)
        addView(textView)

        // 默认隐藏
        visibility = View.GONE
    }

    /**
     * 显示加载中
     * @param msg 提示文字，默认为"加载中..."
     */
    fun showLoading(msg: String = "加载中...") {
        textView.text = msg
        visibility = View.VISIBLE
    }

    /**
     * 隐藏加载中
     */
    fun hideLoading() {
        visibility = View.GONE
    }
}