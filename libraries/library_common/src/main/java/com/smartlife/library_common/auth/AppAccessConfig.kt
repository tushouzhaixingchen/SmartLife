package com.smartlife.library_common.auth

enum class LoginMode { FORCE_LOGIN, GUEST }   // 强制登录 / 游客可浏览
object AppAccessConfig {
    @Volatile var loginMode: LoginMode = LoginMode.FORCE_LOGIN   // 改这一行即切换两种场景
    // 运行时可切：suspend fun toggle(){ ... DataStoreUtils.putString("debug_login_mode", next.name) }
}