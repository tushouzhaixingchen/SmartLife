package com.smartlife.feature_user.data.repository.strategy

import com.smartlife.feature_user.data.datastore.AuthDataStore
import com.smartlife.feature_user.data.remote.LocalAuthDataSource
import com.smartlife.library_common.auth.AuthOutcome
import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_common.auth.AuthStrategy
import com.smartlife.library_common.auth.AuthType

class EmailPwdAuthStrategy(private val local: LocalAuthDataSource,
                            private val authStore: AuthDataStore
) : AuthStrategy {
    override val type = AuthType.EMAIL_CODE
    override suspend fun submit(cred: Map<String,String>): AuthOutcome {
        val email = cred["email"] ?: return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        if (!Regex("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$").matches(email))
            return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        if (!local.verifyCode(cred["code"] ?: "")) return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        val (a,r)=local.mockToken(email); authStore.saveLoginState(email,a,r)
        // TODO: 这里跟手机号一样，应该去查库拿到真实 uid，不要写死 "0"
        return AuthOutcome.Success(AuthSession(a, 7200, "0", email))
        // TODO 阶段2：local.sendCode 换成 RemoteAuthDataSource.sendEmailCode()（SMTP/邮件推送服务）
    }
}