package com.smartlife.library_common.utils

import android.content.SharedPreferences

/**
 * SharedPreferences 工具类
 * 通过 Hilt 注入 SharedPreferences 实例后使用
 * 也可以直接通过 companion object 的 init() 方法初始化后静态调用
 */
object SPUtils {

    private lateinit var prefs: SharedPreferences

    /**
     * 初始化，传入 Hilt 提供的 SharedPreferences 实例
     */
    fun init(sharedPreferences: SharedPreferences) {
        prefs = sharedPreferences
    }

    // ===== 存 =====

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    // ===== 取 =====

    fun getString(key: String, default: String = ""): String {
        return prefs.getString(key, default) ?: default
    }

    fun getInt(key: String, default: Int = 0): Int {
        return prefs.getInt(key, default)
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return prefs.getLong(key, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(key, default)
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return prefs.getFloat(key, default)
    }

    // ===== 删 =====

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    // ===== 查 =====

    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }
}