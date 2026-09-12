package com.smartlife.shell_bottom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smartlife.library_common.model.TabConfig

/**
 * 数据驱动的底部导航栏组件
 * 不写死任何Tab，完全由外部传入TabConfig列表渲染
 */
class BottomNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var bottomNavigation: BottomNavigationView? = null
    private var viewPager: ViewPager2? = null

    // Tab配置列表（外部注入）
    private var tabConfigs: List<TabConfig> = emptyList()

    // Tab切换回调（外部注入）
    var onTabSelectedListener: ((String) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_nav, this, true)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        viewPager = findViewById(R.id.view_pager)

        setupNavigation()
    }

    private fun setupNavigation() {
        // 监听ViewPager切换
        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position in tabConfigs.indices) {
                    bottomNavigation?.selectedItemId = tabConfigs[position].id.hashCode().toLong().toInt()
                    onTabSelectedListener?.invoke(tabConfigs[position].id)
                }
            }
        })

        // 监听Navigation点击
        bottomNavigation?.setOnItemSelectedListener { item ->
            val position = tabConfigs.indexOfFirst { it.id.hashCode().toLong().toInt() == item.itemId }
            if (position >= 0) {
                viewPager?.currentItem = position
            }
            true
        }
    }

    /**
     * 外部调用此方法注入Tab配置
     */
    fun setupTabs(tabs: List<TabConfig>) {
        tabConfigs = tabs
        // TODO: 根据tabs动态构建BottomNavigationView的Menu
        // TODO: 设置ViewPager2的FragmentStateAdapter
    }
}