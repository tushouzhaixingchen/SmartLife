package com.smartlife.feature_user.data.repository

import com.smartlife.feature_user.data.datastore.AuthDataStore
import com.smartlife.feature_user.data.local.dao.UserDao
import com.smartlife.feature_user.data.local.entity.UserEntity
import com.smartlife.feature_user.data.remote.LocalAuthDataSource
import com.smartlife.library_common.auth.AuthOutcome
import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_common.auth.AuthStrategy
import com.smartlife.library_common.auth.AuthType
import com.smartlife.library_common.base.BaseRepository  // safeApiCall 来自此基类
import javax.inject.Inject
import javax.inject.Singleton
// 注：PhoneSms/AccountPwd/EmailCode 三个策略与本类同包，无需 import

@Singleton
class UserRepository @Inject constructor(
    private val local: LocalAuthDataSource, private val userDao: UserDao,
    private val authStore: AuthDataStore
) : BaseRepository(), AuthRepository {
    private val strategies: Map<AuthType, AuthStrategy> by lazy {
        listOf(PhoneSmsAuthStrategy(local,authStore,userDao),
            AccountPwdAuthStrategy(userDao),
            EmailCodeAuthStrategy(local,authStore))
            // .also { AuthProviderRegistry.register(it) }   // 也可在 Application 里统一注册
            .associateBy { it.type }
    }
    override suspend fun sendCode(type: AuthType, account: String) = safeApiCall {
        when(type){                                              // TODO 阶段2 换远程实现
            AuthType.PHONE_SMS, AuthType.EMAIL_CODE -> local.sendCode(account)
            else -> error("该登录方式无需验证码") }
    }
    override suspend fun login(type: AuthType, credential: Map<String,String>) = safeApiCall {
        val st = strategies[type] ?: error("暂不支持该登录方式")
        when(val out = st.submit(credential)) {
            is AuthOutcome.Success -> out.session
            is AuthOutcome.Failure -> error(out.msg)             // BaseRepository 包成 Result.Failure
            is AuthOutcome.NeedMoreStep -> error(out.reason)
        }
    }
    override suspend fun register(phone:String, code:String, nickname:String) = safeApiCall {
        if(!local.verifyCode(code)) error("验证失败")
        if(userDao.findByPhone(phone)!=null) error("该手机号已注册")
        userDao.insertUser(UserEntity(phone=phone, nickname=nickname.trim())); Unit }
}