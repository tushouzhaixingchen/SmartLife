package com.smartlife.feature_user.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smartlife.feature_user.data.local.dao.UserDao
import com.smartlife.feature_user.data.local.entity.UserEntity

@Database(entities=[UserEntity::class], version=4, exportSchema=false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile private var INSTANCE: UserDatabase? = null
        fun getInstance(c: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(c.applicationContext,
                UserDatabase::class.java, "smart_life_user.db").
            fallbackToDestructiveMigration().
            build().
            also { INSTANCE = it }
        }
    }
}