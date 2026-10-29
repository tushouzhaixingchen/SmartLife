package com.smartlife.library_common.auth

interface AuthStrategy {
    val type: AuthType
    suspend fun submit(credential: Map<String,String>): AuthOutcome  // 各方式自定义入参
    fun isEnabled(): Boolean = true                                   // 供开关过滤
}