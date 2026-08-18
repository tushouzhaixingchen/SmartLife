package com.smartlife.library_common.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

/**
 * Room 通用类型转换器
 * 用于 Room 无法直接存储的类型与数据库可存储类型之间的转换
 */
class Converters {

    private val gson = Gson()

    // ===== Date ↔ Long =====

    /**
     * 将时间戳（Long）转为 Date
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    /**
     * 将 Date 转为时间戳（Long）
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    // ===== List<String> ↔ String =====

    /**
     * 将 JSON 字符串转为 List<String>
     */
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        if (value == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    /**
     * 将 List<String> 转为 JSON 字符串
     */
    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        return gson.toJson(list)
    }

    // ===== List<Int> ↔ String =====

    /**
     * 将 JSON 字符串转为 List<Int>
     */
    @TypeConverter
    fun fromIntList(value: String?): List<Int>? {
        if (value == null) return null
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    /**
     * 将 List<Int> 转为 JSON 字符串
     */
    @TypeConverter
    fun toIntList(list: List<Int>?): String? {
        return gson.toJson(list)
    }
}