/*
修复已全部完成，Gradle 正在后台编译，完成后我会告知你结果。

先总结一下发现的问题（衔接断裂点）：

1. 根本原因： BottomNavBar.kt 的 setupTabs() 是空实现 ——只把 tabs 存了起来，里面全是 TODO，从未把 Tab 添加到 BottomNavigationView 的 Menu 中。Menu 为空时 BottomNavigationView 高度为 0，所以整个导航栏看不见。
2. 回调链路也是断的 ： onTabSelectedListener 原来只在 ViewPager2 的翻页回调里触发，但 ViewPager2 从未设置过 Adapter，所以你在 MainActivity 里写的 Fragment 切换永远不会执行。
3. 两套页面容器冲突 ：MainActivity 布局里有 fragment_container ，而 BottomNavBar 内部又塞了一个 ViewPager2 想自己管页面——两套机制并存且都没接通。另外 ViewPager2 在你的 libs.versions.toml 里根本没声明依赖，原代码实际上也编译不过。
修复方式（符合你现有的解耦设计：shell 只负责渲染导航 + 回调 tabId，app 负责装配 Fragment）：

- 4 个分辨率版本的 view_bottom_nav.xml 移除 ViewPager2，只保留 BottomNavigationView；
- setupTabs() 动态构建 Menu，用基于位置的稳定正数 ID（替代可能为负/碰撞的 String.hashCode() ），点击时切换 outline/filled 图标并直接回调；
- MainActivity 默认首页改为复用 fragmentMap 中的实例。
另外提醒：TabConfig 里的 isCenterHighlight （AI 中间凸起按钮）目前 BottomNavigationView 原生不支持，需要后续叠加 FAB/自定义 View 实现，这次没有改动它。
*/

package com.smartlife.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smartlife.app.databinding.ActivityMainBinding
import com.smartlife.library_common.model.TabConfig
import com.smartlife.feature_home.HomeFragment
import com.smartlife.feature_music.MusicFragment
import com.smartlife.feature_ai.AiFragment
import com.smartlife.feature_notes.NotesFragment
import com.smartlife.feature_user.UserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)//binding.root是布局文件的根View（例如LinearLayout、ConstraintLayout等）。

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

        // 组装Tab与Fragment的映射
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

        // 默认显示首页（复用fragmentMap中的实例，避免重复创建）
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentMap.getValue("home"))
            .commit()
    }
}