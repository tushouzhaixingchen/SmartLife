package com.smartlife.library_common.db

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * 数据库实体基类
 * 所有 Room 实体类都应该继承此类
 * 提供统一的 id、创建时间、更新时间字段
 */
open class BaseEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = System.currentTimeMillis()
)