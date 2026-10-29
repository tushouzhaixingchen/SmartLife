package com.smartlife.feature_user.data.repository

import com.smartlife.feature_user.data.datastore.AuthDataStore
import com.smartlife.feature_user.data.local.dao.UserDao
import com.smartlife.feature_user.data.remote.LocalAuthDataSource
import com.smartlife.library_common.auth.AuthOutcome
import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_common.auth.AuthStrategy
import com.smartlife.library_common.auth.AuthType
import com.smartlife.library_common.auth.LoginSession
import com.smartlife.library_common.core.Result

interface AuthRepository {
    suspend fun sendCode(type: AuthType, account: String): Result<Unit>
    suspend fun login(type: AuthType, credential: Map<String, String>): Result<AuthSession>
    suspend fun register(phone: String, code: String, nickname: String): Result<Unit>
}

class PhoneSmsAuthStrategy(private val local: LocalAuthDataSource,
                           private val authStore: AuthDataStore, private val userDao: UserDao) : AuthStrategy {
    override val type = AuthType.PHONE_SMS
    override suspend fun submit(cred: Map<String,String>): AuthOutcome {
        val phone = cred["phone"] ?: return AuthOutcome.Failure("NO_PARAM","验证失败")
        val code  = cred["code"]  ?: return AuthOutcome.Failure("NO_PARAM","验证失败")
        if (!local.verifyCode(code)) return AuthOutcome.Failure("AUTH_FAILED","验证失败") // 规范4.2 统一错误
        val (a,r) = local.mockToken(phone); authStore.saveLoginState(phone,a,r)
        LoginSession.set(true)
        return AuthOutcome.Success(AuthSession(a, 7200, userDao.findByPhone(phone)?.uid?.toString() ?: "0", phone))
    }
}

class AccountPwdAuthStrategy(private val userDao: UserDao) : AuthStrategy {
    override val type = AuthType.ACCOUNT_PWD
    override suspend fun submit(cred: Map<String,String>): AuthOutcome {
        val acc = cred["account"] ?: return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        val pwd = cred["password"] ?: return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        val u = userDao.findByPhone(acc) ?: return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        // TODO 阶段2：改为后端校验 password_hash（客户端绝不存明文/不参与比对）
        return AuthOutcome.Success(AuthSession("mock_access_$acc", 7200, u.uid.toString(), u.nickname))
    }
}

class EmailCodeAuthStrategy(private val local: LocalAuthDataSource,
                            private val authStore: AuthDataStore) : AuthStrategy {
    override val type = AuthType.EMAIL_CODE
    override suspend fun submit(cred: Map<String,String>): AuthOutcome {
        val email = cred["email"] ?: return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        if (!Regex("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$").matches(email))
            return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        if (!local.verifyCode(cred["code"] ?: "")) return AuthOutcome.Failure("AUTH_FAILED","验证失败")
        val (a,r)=local.mockToken(email); authStore.saveLoginState(email,a,r)
        return AuthOutcome.Success(AuthSession(a,7200,"0",email))
        // TODO 阶段2：local.sendCode 换成 RemoteAuthDataSource.sendEmailCode()（SMTP/邮件推送服务）
    }
}

// 注：api.wechatLogin(code).toOutcome() 的 toOutcome() 为 Resp->AuthOutcome 扩展函数，需一并 import 其所在文件包

/*class WechatAuthStrategy(private val api: AuthApi, private val wxApi: IWXAPI) : AuthStrategy {
    override val type = AuthType.WECHAT
    override suspend fun submit(cred: Map<String,String>): AuthOutcome {
        // 1) 拉起微信授权，在 WXEntryActivity 回调里把 authCode 续回来（协程化）
        val code = suspendCancellableCoroutine<String> { cont ->
            wxApi.sendReq(SendAuth.Req().apply { *//* appId, scope="snsapi_userinfo", state *//* })
            // WXEntryActivity: (context as? WechatAuthCallback)?.resume(it.authCode) -> cont.resume(...)
        }
        // 2) 把 code 交给自家后端换业务 token —— 客户端不碰 AppSecret
        return api.wechatLogin(code).toOutcome()   // TODO 需后端 + 微信开放平台资质，本地无法跑通
    }
}*/
// QQ 同构：换 SDK 与回调 Activity，最终都收敛成「拿 code -> POST 自家后端」，故接口稳定。