package com.smartlife.feature_user.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.smartlife.feature_user.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao interface UserDao {
    @Insert suspend fun insertUser(user: UserEntity): Long
    @Query("SELECT * FROM user WHERE phone = :phone LIMIT 1")
    suspend fun findByPhone(phone: String): UserEntity?
}