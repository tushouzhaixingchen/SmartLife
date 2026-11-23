package com.smartlife.feature_user.data.repository.strategy

import com.smartlife.feature_user.data.local.dao.UserDao
import com.smartlife.library_common.auth.AuthOutcome
import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_common.auth.AuthStrategy
import com.smartlife.library_common.auth.AuthType

class AccountPwdAuthStrategy(private val userDao: UserDao) : AuthStrategy {
    override val type = AuthType.ACCOUNT_PWD
    override suspend fun submit(cred: Map<String,String>): AuthOutcome {
        val acc = cred["account"] ?: return AuthOutcome.Failure("AUTH_FAILED","请输入账号")
        val pwd = cred["password"] ?: return AuthOutcome.Failure("AUTH_FAILED","请输入密码")
        //智能查库逻辑（手机/邮箱/自定义账号）
        val u=if(acc.contains("@")){
            userDao.findByEmail(acc)
        }
        else if (acc.length==11&& acc.all{it.isDigit()}){
            userDao.findByPhone(acc)
        }
        else{
            userDao.findByEmail(acc)
        }?:return AuthOutcome.Failure("AUTH_FAILED","账号或密码错误")

        // TODO 阶段2：改为后端校验 password_hash（客户端绝不存明文/不参与比对）
        return AuthOutcome.Success(
            AuthSession(
                "mock_access_$acc",
                7200,
                u?.id.toString(),
                u?.nickname
            )
        )
    }
}