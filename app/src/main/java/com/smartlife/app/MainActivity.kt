package com.smartlife.app

import android.os.Bundle
import android.view.LayoutInflater
import com.smartlife.app.databinding.ActivityMainBinding
import com.smartlife.library_common.auth.AppAccessConfig
import com.smartlife.library_common.auth.FeatureKey
import com.smartlife.library_common.auth.LoginGuard
import com.smartlife.library_common.auth.LoginLauncher
import com.smartlife.library_common.auth.LoginMode
import com.smartlife.library_common.base.BaseActivity
import com.smartlife.library_common.model.TabConfig
import com.smartlife.feature_ai.AiFragment
import com.smartlife.feature_home.HomeFragment
import com.smartlife.feature_music.MusicFragment
import com.smartlife.feature_notes.NotesFragment
import com.smartlife.feature_user.ui.profile.UserFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 强制登录门禁（游客模式直接跳过）
        if (AppAccessConfig.loginMode == LoginMode.FORCE_LOGIN && !LoginGuard.isLogin()) {
            LoginLauncher.launchLogin(this)
            finish()
            return
        }

        // 只有首次创建才组装底部导航，避免 Activity 重建时 Fragment 叠加
        if (savedInstanceState == null) {
            setupBottomNavigation()
        }
    }

    private fun setupBottomNavigation() {
        val tabs = listOf(
            TabConfig("home", "首页",
                com.smartlife.feature_home.R.drawable.ic_home_outline,
                com.smartlife.feature_home.R.drawable.ic_home_filled),
            TabConfig("music", "音乐",
                com.smartlife.feature_music.R.drawable.ic_music_outline,
                com.smartlife.feature_music.R.drawable.ic_music_filled),
            TabConfig("ai", "AI",
                com.smartlife.feature_ai.R.drawable.ic_ai_outline,
                com.smartlife.feature_ai.R.drawable.ic_ai_filled,
                isCenterHighlight = true),
            TabConfig("notes", "笔记",
                com.smartlife.feature_notes.R.drawable.ic_notes_outline,
                com.smartlife.feature_notes.R.drawable.ic_notes_filled),
            TabConfig("mine", "我的",
                com.smartlife.feature_user.R.drawable.ic_mine_outline,
                com.smartlife.feature_user.R.drawable.ic_mine_filled)
        )

        val fragmentMap = mapOf(
            "home" to HomeFragment(),
            "music" to MusicFragment(),
            "ai" to AiFragment(),
            "notes" to NotesFragment(),
            "mine" to UserFragment()
        )

        binding.bottomNavBar?.setupTabs(tabs)

        binding.bottomNavBar?.onTabSelectedListener = { tabId ->
            val key = when (tabId) {
                "ai" -> FeatureKey.AI_CHAT
                "notes" -> FeatureKey.CREATE_NOTE
                "mine" -> FeatureKey.MINE_PROFILE
                else -> null
            }
            LoginGuard.guard(
                activity = this,
                key = key,
                onGranted = {
                    fragmentMap[tabId]?.let { fragment ->
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                    }
                }
            )
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentMap.getValue("home"))
            .commit()
    }
}