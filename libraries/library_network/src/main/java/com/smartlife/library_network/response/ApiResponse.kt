package com.smartlife.library_network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 统一的后端响应结构
 * 对应 JSON: { "code": 200, "message": "success", "data": { ... } }
 * 遵循总纲 6.3 网络层公共组件 规范
 *
 * 注意：此类仅用于网络层的 JSON 解析，不应在业务层或 UI 层直接使用。
 */
@Serializable
data class ApiResponse<T>(
    @SerialName("code") val code: Int = 0,
    @SerialName("message") val message: String = "",
    @SerialName("data") val data: T? = null
) {
    fun isSuccess(): Boolean = code == 200
}