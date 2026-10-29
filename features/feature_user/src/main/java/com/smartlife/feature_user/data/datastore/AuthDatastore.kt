package com.smartlife.feature_user.data.datastore

import com.smartlife.library_common.utils.DataStoreUtils
import kotlinx.coroutines.flow.Flow

class AuthDataStore {
    private object Key { const val LOGGED_IN="is_logged_in"; const val USERNAME="username"
        const val TOKEN="access_token"; const val REFRESH="refresh_token" }
    val isLoggedIn: Flow<Boolean> = DataStoreUtils.getBooleanFlow(Key.LOGGED_IN, false)
    suspend fun saveLoginState(username:String, token:String, refresh:String) {
        DataStoreUtils.putBoolean(Key.LOGGED_IN, true)
        DataStoreUtils.putString(Key.USERNAME, username)
        DataStoreUtils.putString(Key.TOKEN, token)      // 阶段3 改 EncryptedSharedPreferences
        DataStoreUtils.putString(Key.REFRESH, refresh)
    }
    suspend fun clearLoginState() {
        DataStoreUtils.remove(Key.LOGGED_IN); DataStoreUtils.remove(Key.USERNAME)
        DataStoreUtils.remove(Key.TOKEN); DataStoreUtils.remove(Key.REFRESH)
    }
}
// 若 DataStoreUtils 方法名不同，按 utils 真实签名替换（规范6.1：复用工具类，别另起炉灶）