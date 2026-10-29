package com.smartlife.feature_user.data.remote

import kotlinx.coroutines.delay

class LocalAuthDataSource {
    companion object { private const val FIXED_CODE = "123456" }
    suspend fun sendCode(account:String){ delay((500L..800L).random()); println("【模拟验证码】$account -> $FIXED_CODE") }
    suspend fun verifyCode(code:String):Boolean { delay((300L..600L).random()); return code==FIXED_CODE }
    suspend fun mockToken(account:String) = Pair("mock_access_$account","mock_refresh_$account")
}