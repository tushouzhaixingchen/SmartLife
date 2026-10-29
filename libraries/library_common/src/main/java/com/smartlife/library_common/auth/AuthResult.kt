package com.smartlife.library_common.auth

data class AuthSession(val token:String, val expiresIn:Long, val userId:String,
                       val nickname:String?=null, val needBindPhone:Boolean=false) // 第三方首登未绑手机=true
sealed interface AuthOutcome {
    data class Success(val session: AuthSession): AuthOutcome
    data class NeedMoreStep(val reason:String): AuthOutcome     // 需绑手机/绑定已有账号
    data class Failure(val code:String, val msg:String): AuthOutcome }