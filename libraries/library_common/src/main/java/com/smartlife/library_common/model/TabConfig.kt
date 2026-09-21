package com.smartlife.library_common.model

/**
 * 底部导航栏Tab配置的数据类
 * 定义在library_common中，供shell_bottom和各feature模块共同依赖
 */
data class TabConfig(
    val id: String,              // 唯一标识，如 "home", "music", "ai", "notes", "mine"
    val title: String,           // 导航栏显示文字
    val iconResId: Int,          // 未选中状态图标资源ID
    val selectedIconResId: Int,  // 选中状态图标资源ID
    val isCenterHighlight: Boolean = false  // 是否为中间凸起按钮（AI Tab）
)