package com.smartlife.library_common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * 网络状态检测工具类
 * 使用 NetworkUtils 需要在 AndroidManifest.xml 中声明网络状态权限：
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
object NetworkUtils {

    /**
     * 判断当前是否有网络连接
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = AppContextUtils.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * 判断当前是否是 WiFi 连接
     */
    fun isWifi(): Boolean {
        val connectivityManager = AppContextUtils.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    /**
     * 判断当前是否是移动数据连接
     */
    fun isMobileData(): Boolean {
        val connectivityManager = AppContextUtils.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}