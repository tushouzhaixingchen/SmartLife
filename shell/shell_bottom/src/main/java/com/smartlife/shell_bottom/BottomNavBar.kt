package com.smartlife.shell_bottom

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smartlife.library_common.model.TabConfig

/**
 * 数据驱动的底部导航栏组件
 * 不写死任何Tab，完全由外部传入TabConfig列表渲染
 *
 * 职责单一：只负责渲染导航UI并把点击的tabId回调出去；
 * 页面(Fragment)由宿主Activity/Fragment自行管理，保证shell不反向依赖业务feature。
 */
/**定义了一个自定义 View 类 BottomNavBar，它继承自 ConstraintLayout，是一个可以直接在 XML 布局中使用的容器组件*/
/**
 * : ConstraintLayout(context, attrs, defStyleAttr)：表示 BottomNavBar 继承 ConstraintLayout，
 * 并将这三个参数原样传给父类构造函数。这样它就拥有了 ConstraintLayout 的所有能力（约束布局、子 View 管理等），
 * 同时可以在此基础上扩展自己的导航栏逻辑。*/
class BottomNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val bottomNavigation: BottomNavigationView

    // Tab配置列表（外部注入）
    private var tabConfigs: List<TabConfig> = emptyList()

    // Tab切换回调（外部注入）
    var onTabSelectedListener: ((String) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_nav, this, true)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        setupAppearance()
    }

    private fun setupAppearance() {
        // 主题是AppCompat，这里显式给出选中/未选中颜色，保证状态色正确
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf()
            ),
            intArrayOf(COLOR_SELECTED, COLOR_UNSELECTED)
        )
        bottomNavigation.itemIconTintList = colorStateList
        bottomNavigation.itemTextColor = colorStateList
        // 始终显示文字（超过3个Tab时默认会置灰未选中文字）
        bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED
    }

    /**
     * 外部调用此方法注入Tab配置，动态构建导航Menu
     */
    fun setupTabs(tabs: List<TabConfig>) {
        tabConfigs = tabs
        bottomNavigation.menu.clear()

        tabs.forEachIndexed { index, tab ->
            // 使用基于位置的稳定正数ID，避免String.hashCode()出现负数或碰撞
            bottomNavigation.menu
                .add(0, itemId(index), index, tab.title)
                .setIcon(if (index == 0) tab.selectedIconResId else tab.iconResId)
        }

        // 点击Tab：更新图标状态并把tabId回调给宿主
        bottomNavigation.setOnItemSelectedListener { item ->
            val position = positionOf(item.itemId)
            val tab = tabConfigs.getOrNull(position) ?: return@setOnItemSelectedListener false
            refreshIcons(position)
            onTabSelectedListener?.invoke(tab.id)
            true
        }
    }

    private fun refreshIcons(selectedPosition: Int) {
        tabConfigs.forEachIndexed { index, tab ->
            bottomNavigation.menu.findItem(itemId(index))?.setIcon(
                if (index == selectedPosition) tab.selectedIconResId else tab.iconResId
            )
        }
    }

    private fun itemId(position: Int) = position + 1

    private fun positionOf(itemId: Int) = itemId - 1

    companion object {
        private const val COLOR_SELECTED = 0xFF1E88E5.toInt()
        private const val COLOR_UNSELECTED = 0xFF9E9E9E.toInt()
    }
}
