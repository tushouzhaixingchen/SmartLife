/**
 * 网络模块：基于 Hilt 提供全局单例的 OkHttpClient 与 Retrofit。
 * 统一配置公共请求头拦截器、日志拦截器及 15 秒超时时间。
 * 使用 Gson 作为 JSON 转换器，BASE_URL 需替换为真实后端地址。
 * 其他类可直接通过 @Inject 注入 Retrofit 使用，无需手动创建。
 */
package com.smartlife.library_network.di

import com.smartlife.library_network.interceptor.ApiInterceptor
import com.smartlife.library_network.interceptor.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO: 这里换成你们真实的后端接口地址
    private const val BASE_URL = "https://api.example.com/"

    /**
     * 提供 OkHttpClient 单例
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // 添加公共请求头拦截器
            .addInterceptor(ApiInterceptor())
            // 添加日志拦截器
            .addInterceptor(LoggingInterceptor())
            // 设置超时时间
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    /**
     * 提供 Retrofit 单例
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            // 使用 Gson 转换器
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}