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