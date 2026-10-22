package com.smartlife.app

import android.os.Bundle
import android.view.LayoutInflater
import com.smartlife.app.databinding.ActivityMainBinding
import com.smartlife.library_common.base.BaseActivity
import com.smartlife.library_common.model.TabConfig
import com.smartlife.feature_home.HomeFragment
import com.smartlife.feature_music.MusicFragment
import com.smartlife.feature_ai.AiFragment
import com.smartlife.feature_notes.NotesFragment
import com.smartlife.feature_user.UserFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    // 实现父类抽象方法：返回本页面的 ViewBinding
    override fun getViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding 已被 BaseActivity 创建并 setContentView，这里直接写业务逻辑

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        // 定义底部导航Tab配置
        val tabs = listOf(
            TabConfig(
                id = "home",
                title = "首页",
                iconResId = com.smartlife.feature_home.R.drawable.ic_home_outline,
                selectedIconResId = com.smartlife.feature_home.R.drawable.ic_home_filled
            ),
            TabConfig(
                id = "music",
                title = "音乐",
                iconResId = com.smartlife.feature_music.R.drawable.ic_music_outline,
                selectedIconResId = com.smartlife.feature_music.R.drawable.ic_music_filled
            ),
            TabConfig(
                id = "ai",
                title = "AI",
                iconResId = com.smartlife.feature_ai.R.drawable.ic_ai_outline,
                selectedIconResId = com.smartlife.feature_ai.R.drawable.ic_ai_filled,
                isCenterHighlight = true
            ),
            TabConfig(
                id = "notes",
                title = "笔记",
                iconResId = com.smartlife.feature_notes.R.drawable.ic_notes_outline,
                selectedIconResId = com.smartlife.feature_notes.R.drawable.ic_notes_filled
            ),
            TabConfig(
                id = "mine",
                title = "我的",
                iconResId = com.smartlife.feature_user.R.drawable.ic_mine_outline,
                selectedIconResId = com.smartlife.feature_user.R.drawable.ic_mine_filled
            )
        )

        // 组装Tab与Fragment的映射（注意：用 mapOf 存的是实例，重复 setup 时会复用，符合你原有逻辑）
        val fragmentMap = mapOf(
            "home" to HomeFragment(),
            "music" to MusicFragment(),
            "ai" to AiFragment(),
            "notes" to NotesFragment(),
            "mine" to UserFragment()
        )

        // 设置底部导航
        binding.bottomNavBar?.setupTabs(tabs)
        binding.bottomNavBar?.onTabSelectedListener = { tabId ->
            fragmentMap[tabId]?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        }

        // 默认显示首页（复用 fragmentMap 中的实例，避免重复创建）
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentMap.getValue("home"))
            .commit()
    }
}