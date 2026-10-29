package com.smartlife.feature_user.data.remote

import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_network.response.ApiResponse// 统一响应包装，按你工程实际包名替换
import retrofit2.http.Body
import retrofit2.http.POST

// 一套 /auth/login 覆盖所有方式：type 决定后端走哪条策略
// POST /auth/login  body: {"type":"phone_sms","phone":"138...","code":"123456"}
//                   body: {"type":"account_pwd","account":"u_1001","password":"***"}
//                   body: {"type":"wechat","code":"<wx auth code>"}
//   resp: {"token":"...","expiresIn":7200,"userId":"...","nickname":"...","needBindPhone":false}
// POST /auth/bind {identityType,code} / POST /auth/unbind {identityType} / POST /auth/refresh {token}
// 失败一律回通用码 { "code":"AUTH_FAILED","msg":"验证失败" }（规范4.2 防枚举）
interface AuthApi {
    @POST("/auth/login")   suspend fun login(@Body body: Map<String,String>): ApiResponse<AuthSession>
    @POST("/auth/login")   suspend fun wechatLogin(@Body body: Map<String,String>): ApiResponse<AuthSession>
    @POST("/auth/refresh") suspend fun refresh(@Body body: Map<String,String>): ApiResponse<AuthSession>
}
// 客户端预留方式：Repository 实现里暂时调本地 mock，把 Result<AuthSession>/AuthOutcome 装配做好；
// 将来只把 mock 那一行换成 api.login(...)，上层零改动。