package com.smartlife.library_network.interceptor

import com.smartlife.library_common.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

/**
 * 网络日志拦截器
 * 打印请求和响应的详细信息，发布时可通过 LogUtils.enable = false 关闭
 */
class LoggingInterceptor : Interceptor {
    companion object {
        private const val TAG = "Network"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 打印请求信息
        logRequest(request)

        // 执行请求并计算耗时
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val duration = System.currentTimeMillis() - startTime

        // 打印响应信息
        logResponse(response, duration)

        return response
    }

    private fun logRequest(request: okhttp3.Request) {
        if (!LogUtils.enable) return

        val sb = StringBuilder()
        sb.appendLine("╔══════ Request ══════")
        sb.appendLine("║ URL: ${request.url}")
        sb.appendLine("║ Method: ${request.method}")

        // 打印请求体
        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            sb.appendLine("║ Body: ${buffer.readUtf8()}")
        }

        sb.appendLine("╚════════════════════")
        LogUtils.d(sb.toString(), TAG)
    }

    private fun logResponse(response: Response, duration: Long) {
        if (!LogUtils.enable) return

        val sb = StringBuilder()
        sb.appendLine("╔══════ Response ══════")
        sb.appendLine("║ URL: ${response.request.url}")
        sb.appendLine("║ Code: ${response.code}")
        sb.appendLine("║ Duration: ${duration}ms")
        sb.appendLine("║ Size: ${response.body?.contentLength() ?: 0} bytes")
        sb.appendLine("╚════════════════════")
        LogUtils.d(sb.toString(), TAG)
    }
}