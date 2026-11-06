package com.smartlife.feature_user.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartlife.library_common.db.BaseEntity

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo(name="phone") val phone: String,             // 注册手机号，唯一
    @ColumnInfo(name="account") val account: String? = null, // 👈 新增：自定义账号（可空）
    @ColumnInfo(name="password") val password: String = "", // 👈 新增：存密码（真实项目存哈希，这里先存明文）
    @ColumnInfo(name="nickname") val nickname: String = ""
) : BaseEntity()                                              // 自动获得 id/createTime/updateTime              // 自动获得 id / createTime / updateTime