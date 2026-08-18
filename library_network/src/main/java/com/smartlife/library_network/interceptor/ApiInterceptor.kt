package com.smartlife.library_network.interceptor

import com.smartlife.library_common.utils.SPUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 公共请求头拦截器
 * 用于统一添加 Token、设备信息等
 */
class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 构建新的请求，添加公共 Header
        val newRequest = originalRequest.newBuilder().apply {
            // 1. 添加 Token (如果已登录)
            val token = try {
                SPUtils.getString("user_token", "")
            } catch (e: Exception) {
                ""
            }
            if (token.isNotEmpty()) {
                header("Authorization", "Bearer $token")
            }

            // 2. 添加其他公共 Header
            header("Content-Type", "application/json")
            header("Accept", "application/json")

            // 可以在这里添加设备ID、App版本号等信息
            // header("X-Device-Id", Build.SERIAL)

        }.build()

        return chain.proceed(newRequest)
    }
}