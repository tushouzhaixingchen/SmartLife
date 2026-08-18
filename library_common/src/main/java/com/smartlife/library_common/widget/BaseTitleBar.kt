package com.smartlife.library_common.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 通用标题栏
 * 包含：左侧返回按钮 + 居中标题 + 右侧操作区域
 *
 * 使用方式（在布局 XML 中）：
 * <com.smartlife.library_common.widget.BaseTitleBar
 *     android:id="@+id/titleBar"
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content" />
 *
 * 在代码中设置：
 * binding.titleBar.setTitle("首页")
 * binding.titleBar.setBackListener { finish() }
 * binding.titleBar.setRightText("保存") { save() }
 */
class BaseTitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val container: LinearLayout
    private val backView: TextView
    private val titleView: TextView
    private val rightView: TextView

    private var onBackClick: (() -> Unit)? = null
    private var onRightClick: (() -> Unit)? = null

    init {
        // 外层容器
        container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                dpToPx(48)
            )
            setPadding(dpToPx(4), 0, dpToPx(12), 0)
        }

        // 左侧返回按钮
        backView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            text = "←"
            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(dpToPx(8), 0, dpToPx(8), 0)
            setOnClickListener {
                onBackClick?.invoke()
            }
        }

        // 居中标题（用 weight=1 占满中间空间）
        titleView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
            gravity = Gravity.CENTER
            textSize = 18f
            setTextColor(Color.BLACK)
            maxLines = 1
        }

        // 右侧操作文字
        rightView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER
            textSize = 14f
            setTextColor(0xFF2196F3.toInt())
            setPadding(dpToPx(8), 0, dpToPx(8), 0)
            visibility = View.GONE
            setOnClickListener {
                onRightClick?.invoke()
            }
        }

        container.addView(backView)
        container.addView(titleView)
        container.addView(rightView)
        addView(container)
    }

    /**
     * 设置标题文字
     */
    fun setTitle(title: String) {
        titleView.text = title
    }

    /**
     * 设置返回按钮点击事件
     */
    fun setBackListener(listener: () -> Unit) {
        onBackClick = listener
    }

    /**
     * 设置右侧文字及点击事件
     * 传入 null 则隐藏右侧按钮
     */
    fun setRightText(text: String?, listener: (() -> Unit)? = null) {
        if (text == null) {
            rightView.visibility = View.GONE
        } else {
            rightView.text = text
            rightView.visibility = View.VISIBLE
            onRightClick = listener
        }
    }

    /**
     * 显示/隐藏返回按钮
     */
    fun showBack(show: Boolean) {
        backView.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 设置标题栏背景色
     */
    fun setBarColor(color: Int) {
        container.setBackgroundColor(color)
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}