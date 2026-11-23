package com.smartlife.feature_user.data.repository

import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_common.auth.AuthType
import com.smartlife.library_common.core.Result

interface AuthRepository {
    suspend fun sendCode(type: AuthType, account: String): Result<Unit>
    suspend fun login(type: AuthType, credential: Map<String, String>): Result<AuthSession>
    suspend fun register(phone: String, code: String, nickname: String): Result<Unit>
}