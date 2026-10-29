package com.smartlife.library_common.auth

enum class FeatureKey { AI_CHAT, CREATE_NOTE, BIND_DEVICE, ORDER, MINE_PROFILE }
object FeatureAccess {
    private val gatedInGuest = setOf(FeatureKey.AI_CHAT, FeatureKey.CREATE_NOTE,
        FeatureKey.BIND_DEVICE, FeatureKey.ORDER, FeatureKey.MINE_PROFILE)
    // 强制登录=全部要登录；游客模式=仅登记项受限，首页/音乐等公开
    fun requireLogin(k: FeatureKey?) =
        AppAccessConfig.loginMode == LoginMode.FORCE_LOGIN || (k != null && k in gatedInGuest)
}