package com.smartlife.library_common.router

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 用户模块对外暴露的服务接口
 *
 * 放在 library_common 中，供其他模块依赖调用
 * 具体实现类放在 user 业务模块中
 *
 * 使用方式：
 * val userService = ARouter.getInstance().navigation(IUserService::class.java)
 * val isLogin = userService?.isLogin() ?: false
 */
interface IUserService : IProvider {

    /**
     * 判断用户是否已登录
     */
    fun isLogin(): Boolean

    /**
     * 获取当前用户 ID
     * 未登录时返回 null
     */
    fun getUserId(): String?

    /**
     * 获取当前用户 Token
     * 未登录时返回 null
     */
    fun getToken(): String?

    /**
     * 退出登录
     */
    fun logout()
}