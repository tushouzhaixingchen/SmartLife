package com.smartlife.feature_user.di

import android.content.Context
import com.smartlife.feature_user.data.datastore.AuthDataStore
import com.smartlife.feature_user.data.local.dao.UserDao
import com.smartlife.feature_user.data.local.database.UserDatabase
import com.smartlife.feature_user.data.remote.LocalAuthDataSource
import com.smartlife.feature_user.data.repository.AuthRepository
import com.smartlife.feature_user.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object UserModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext c: Context) = UserDatabase.getInstance(c)
    @Provides @Singleton fun provideUserDao(db: UserDatabase) = db.userDao()
    @Provides @Singleton fun provideAuthDataStore() = AuthDataStore()
    @Provides @Singleton fun provideLocal() = LocalAuthDataSource()
    @Provides @Singleton fun provideAuthRepository(impl: UserRepository): AuthRepository = impl
}
// Application 加 @HiltAndroidApp，onCreate 里 AppContextUtils.init(this); DataStoreUtils.init(this)
// （规范6.1.8）。LoginGuard 是普通 object、只依赖 DataStoreUtils，无需 Hilt 注册。