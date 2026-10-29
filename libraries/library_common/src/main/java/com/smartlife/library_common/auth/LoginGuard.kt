package com.smartlife.library_common.auth

import android.app.Activity
import com.smartlife.library_common.utils.ToastUtils
import java.lang.ref.WeakReference

/**
 * 统一鉴权入口（最小可用版）。
 * 现阶段规则：未登录就拦、跳登录；登录成功后续跑被拦的动作。
 * 以后要加「游客 / 强制登录」开关，只改 FeatureAccess.requireLogin() 一处，全工程调用点不动。
 */
object LoginGuard {

    /** 纯内存读，不再 runBlocking，不再碰 DataStore */
    fun isLogin(): Boolean = LoginSession.isLoggedIn()

    // 登录后续跑的动作 + 发起它的页面（弱引用，避免持有已销毁 Activity 造成泄漏或崩溃）
    private var pendingAction: (() -> Unit)? = null
    private var pendingHost: WeakReference<Activity>? = null

    fun guard(
        activity: Activity,
        key: FeatureKey? = null,
        onGranted: () -> Unit,
        onDenied: () -> Unit = {}
    ) {
        val needLogin = FeatureAccess.requireLogin(key)
        if (!needLogin || isLogin()) {
            onGranted()          // 公开功能 / 已登录 -> 放行
            return
        }
        pendingAction = onGranted
        pendingHost = WeakReference(activity)
        ToastUtils.showShort("请先登录后使用该功能")
        LoginLauncher.launchLogin(activity)    // 跳登录
        onDenied()
    }

    /**
     * 登录成功后调。
     * @return true = 确实有被拦的动作且执行了；false = 没有待续跑动作，或宿主已销毁
     */
    fun runPendingAction(): Boolean {
        val action = pendingAction ?: return false
        val host = pendingHost?.get()
        pendingAction = null
        pendingHost = null
        // 宿主页面已经 finish / destroy，就不要执行了，否则操作死 Activity 会崩
        if (host == null || host.isFinishing || host.isDestroyed) return false
        action()
        return true
    }
}