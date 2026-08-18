package com.smartlife.library_common.router

/**
 * ARouter 路由路径常量
 * 统一管理所有模块的路由地址，避免硬编码字符串散落各处
 *
 * 命名规范：/模块名/页面名
 * 例如：/home/main 表示 home 模块的主页面
 */
object RouterPath {

    /**
     * Home 模块（首页）
     */
    object Home {
        private const val HOME = "/home"
        const val MAIN = "$HOME/main"
        const val DETAIL = "$HOME/detail"
    }

    /**
     * User 模块（用户中心）
     */
    object User {
        private const val USER = "/user"
        const val LOGIN = "$USER/login"
        const val PROFILE = "$USER/profile"
        const val SETTINGS = "$USER/settings"
    }

    /**
     * Common 模块（公共页面）
     */
    object Common {
        private const val COMMON = "/common"
        const val WEB_VIEW = "$COMMON/web_view"
        const val SCAN = "$COMMON/scan"
    }
}