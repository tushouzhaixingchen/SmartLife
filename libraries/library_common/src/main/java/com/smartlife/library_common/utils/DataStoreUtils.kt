package com.smartlife.library_common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

/**
 * DataStore 工具类：基于 Jetpack DataStore 封装，用于替代 SharedPreferences。
 * 提供 String/Int/Long/Boolean/Float 五种类型的存、取、删、查及 Flow 监听能力。
 * 写入操作通过内部 IO 协程异步执行；读取提供同步（兼容旧代码）与 Flow 两种方式。
 * 使用前需在 Application 中调用 init(context) 初始化，默认文件名 "smart_life_prefs"。
 */
object DataStoreUtils {
    // 1. 声明内部的协程作用域（专门用于后台写入）
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // 1. 声明 DataStore 变量
    private lateinit var dataStore: DataStore<Preferences>

    /**
     * 初始化
     */
    fun init(context: Context) {
        // 2. 使用 Factory 创建实例（推荐命名 "smart_life_prefs"）
        dataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = null, // 如果文件损坏，手动处理（null 则抛出异常）
            migrations = emptyList(),
        ) {
            context.preferencesDataStoreFile("smart_life_prefs")
        }
    }

    // ==================== 存 ====================

    fun putString(key: String, value: String) {
        // 3. 使用 scope 启动协程来执行 edit
        scope.launch {
            dataStore.edit { prefs ->
                prefs[stringPreferencesKey(key)] = value
            }
        }
    }

    fun putInt(key: String, value: Int) {
        scope.launch {
            dataStore.edit { prefs ->
                prefs[intPreferencesKey(key)] = value
            }
        }
    }

    fun putLong(key: String, value: Long) {
        scope.launch {
            dataStore.edit { prefs ->
                prefs[longPreferencesKey(key)] = value
            }
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        scope.launch {
            dataStore.edit { prefs ->
                prefs[booleanPreferencesKey(key)] = value
            }
        }
    }

    fun putFloat(key: String, value: Float) {
        scope.launch {
            dataStore.edit { prefs ->
                prefs[floatPreferencesKey(key)] = value
            }
        }
    }

    // ==================== 取（同步返回值，兼容旧代码）====================

    fun getString(key: String, default: String = ""): String {
        return getStringFlow(key, default).getValueOrNull() ?: default
    }

    fun getInt(key: String, default: Int = 0): Int {
        return getIntFlow(key, default).getValueOrNull() ?: default
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return getLongFlow(key, default).getValueOrNull() ?: default
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getBooleanFlow(key, default).getValueOrNull() ?: default
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return getFloatFlow(key, default).getValueOrNull() ?: default
    }

    // ==================== 删 ====================

    fun remove(key: String) {
        scope.launch {
            dataStore.edit { prefs ->
                prefs.remove(stringPreferencesKey(key))
            }
        }
    }

    fun clear() {
        scope.launch {
            dataStore.edit { prefs ->
                prefs.clear()
            }
        }
    }

    // ==================== 查 ====================

    fun contains(key: String): Boolean {
        return dataStore.data
            .map { prefs -> prefs.contains(stringPreferencesKey(key)) }
            .catch { e ->
                emit(false)
            }
            .getValueOrNull() ?: false
    }

    // ==================== Flow 版本（推荐在 ViewModel/Fragment 中使用）====================

    fun getStringFlow(key: String, default: String = ""): Flow<String> {
        return dataStore.data
            .onStart { emit(emptyPreferences()) }
            .catch { e -> emit(emptyPreferences()) }
            .map { prefs -> prefs[stringPreferencesKey(key)] ?: default }
    }

    fun getIntFlow(key: String, default: Int = 0): Flow<Int> {
        return dataStore.data
            .onStart { emit(emptyPreferences()) }
            .catch { e -> emit(emptyPreferences()) }
            .map { prefs -> prefs[intPreferencesKey(key)] ?: default }
    }

    fun getLongFlow(key: String, default: Long = 0L): Flow<Long> {
        return dataStore.data
            .onStart { emit(emptyPreferences()) }
            .catch { e -> emit(emptyPreferences()) }
            .map { prefs -> prefs[longPreferencesKey(key)] ?: default }
    }

    fun getBooleanFlow(key: String, default: Boolean = false): Flow<Boolean> {
        return dataStore.data
            .onStart { emit(emptyPreferences()) }
            .catch { e -> emit(emptyPreferences()) }
            .map { prefs -> prefs[booleanPreferencesKey(key)] ?: default }
    }

    fun getFloatFlow(key: String, default: Float = 0f): Flow<Float> {
        return dataStore.data
            .onStart { emit(emptyPreferences()) }
            .catch { e -> emit(emptyPreferences()) }
            .map { prefs -> prefs[floatPreferencesKey(key)] ?: default }
    }

    // ==================== 内部扩展：阻塞式取值 ====================

    /**
     * 在协程外部同步获取 Flow 的值（仅用于兼容旧代码的临时方法）
     * 注意：不要在协程内调用此方法，会阻塞线程
     */
    private fun <T> Flow<T>.getValueOrNull(): T? {
        var value: T? = null
        // 使用 runBlocking 阻塞等待 Flow 发出第一个值
        runBlocking {
            this@getValueOrNull.first()
        }
        return value
    }
}