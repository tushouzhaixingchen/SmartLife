package com.smartlife.library_common.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module //这表示一个模块，没有这个注解，Hilt将不会识别这个类
@InstallIn(SingletonComponent::class) //这表示这个模块将提供单例实例
object AppModule { //这里使用object关键字，表示这是一个单例对象，里面的方法都是静态的，不需要创建对象就可以调用
    /*提供全局Application Context*/
    @Provides //告诉hilt，这个代码用来提供依赖实例
    @Singleton
    fun provideContext(application: Application): Context =application.
    applicationContext

    /*
    * 提供全局SharedPreferences实例
    * 默认文件名：“app_prefs”
    * Context.MODE_PRIVATE: 私有模式，文件只能被应用访问
    * */
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
}