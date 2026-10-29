package com.smartlife.library_common.auth

import android.util.Log
import com.smartlife.library_common.utils.DataStoreUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

/**
 * 登录态内存缓存（Single Source of Truth 的读取端）
 *
 * - 启动时 restore() 从 DataStore 读一次盘，之后所有读取都是内存级
 * - 登录 / 登出时调用 set()，同步更新内存 + 落盘
 * - 所有业务代码（LoginGuard / Fragment / ViewModel）判断登录态一律走 isLoggedIn()
 */
object LoginSession {

    private const val TAG = "LoginSession"
    private const val KEY_LOGGED_IN = "is_logged_in"   // 与 AuthDataStore 的键保持一致

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn.asStateFlow()

    @Volatile
    private var restored = false

    /**
     * 在 SmartLifeApp.onCreate() 里调用一次。
     * 用 runBlocking 同步读一次盘，保证 MainActivity 启动时拿到的一定是最新值。
     * 只阻塞启动阶段这一次，用户看到的是启动画面，无感。
     */
    fun restore() {
        if (restored) return
        _loggedIn.value = try {
            runBlocking(Dispatchers.IO) {
                DataStoreUtils.getBoolean(KEY_LOGGED_IN, false)
            }
        } catch (e: Throwable) {
            Log.e(TAG, "restore failed, fallback to false", e)
            false
        }
        restored = true
    }

    /**
     * 登录成功 / 登出时调用。
     * 内存和磁盘一起改，保证进程内外一致。
     */
    fun set(loggedIn: Boolean, persist: Boolean = true) {
        _loggedIn.value = loggedIn
        restored = true
        if (persist) {
            DataStoreUtils.putBoolean(KEY_LOGGED_IN, loggedIn)
        }
    }

    /** 纯内存读，主线程随时可调 */
    fun isLoggedIn(): Boolean = _loggedIn.value
}