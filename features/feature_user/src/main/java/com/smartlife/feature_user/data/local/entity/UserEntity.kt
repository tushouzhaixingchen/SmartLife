package com.smartlife.feature_user.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartlife.library_common.db.BaseEntity

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="uid") val uid: Long = 0,
    @ColumnInfo(name="phone") val phone: String,             // 注册手机号，唯一
    @ColumnInfo(name="nickname") val nickname: String = ""
) : BaseEntity()                                              // 自动获得 id/createTime/updateTime              // 自动获得 id / createTime / updateTime