package com.smartlife.feature_user.data.repository.strategy

import com.smartlife.feature_user.data.datastore.AuthDataStore
import com.smartlife.feature_user.data.local.dao.UserDao
import com.smartlife.feature_user.data.local.entity.UserEntity
import com.smartlife.feature_user.data.remote.LocalAuthDataSource
import com.smartlife.library_common.auth.AuthOutcome
import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_common.auth.AuthStrategy
import com.smartlife.library_common.auth.AuthType
import com.smartlife.library_common.auth.LoginSession

class PhoneSmsAuthStrategy(private val local: LocalAuthDataSource,
                           private val authStore: AuthDataStore, private val userDao: UserDao
) : AuthStrategy {
    override val type = AuthType.PHONE_SMS
    override suspend fun submit(cred: Map<String,String>): AuthOutcome {
        val phone = cred["phone"] ?: return AuthOutcome.Failure("NO_PARAM","验证失败")
        val code  = cred["code"]  ?: return AuthOutcome.Failure("NO_PARAM","验证失败")
        if (!local.verifyCode(code)) return AuthOutcome.Failure("AUTH_FAILED","验证失败") // 规范4.2 统一错误
        // 2. 👇 查库（体验模式：未注册自动注册）
        var user = userDao.findByPhone(phone)
        if (user == null) {
            // 没查到，说明是新用户，直接执行插入
            val newUser = UserEntity(
                phone = phone,
                nickname = "用户${phone.takeLast(4)}" // 默认昵称取手机号后4位
            )
            // 👈 insertUser 方法必须返回 Long（Room 自动生成的自增 uid）
            val newId = userDao.insertUser(newUser)

            // 将生成的 uid 赋给对象，方便后续封装 Session
            newUser.id = newId
            user = newUser
        }

        // 3. 生成 token 并保存状态
        val (a, r) = local.mockToken(phone)
        authStore.saveLoginState(phone, a, r)
        LoginSession.set(true)

        // 4. 返回 Session
        return AuthOutcome.Success(
            AuthSession(a, 7200, user.id.toString() ?: "0", user.nickname)
        )
    }
}