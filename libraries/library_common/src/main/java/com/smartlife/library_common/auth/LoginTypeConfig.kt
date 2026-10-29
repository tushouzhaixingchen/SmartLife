package com.smartlife.library_common.auth

object LoginTypeConfig {
    @Volatile var allowedTypes: Set<AuthType> = setOf(AuthType.PHONE_SMS)  // 默认只开手机
    fun isAllowed(t: AuthType) = t in allowedTypes
    suspend fun save()    { /* DataStoreUtils.putStringSet("allowed_auth_types", ...) */ }
    suspend fun restore() { /* 从 DataStore 读回，无则保持默认 */ }
}