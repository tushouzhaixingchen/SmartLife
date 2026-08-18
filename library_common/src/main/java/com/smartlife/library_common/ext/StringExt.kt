package com.smartlife.library_common.ext

/**
 * String 扩展函数
 */

/**
 * 判断字符串是否为空或空白
 * 比原生的 isNullOrBlank() 更语义化
 */
fun String?.isEmptyOrBlank(): Boolean {
    return this.isNullOrBlank()
}

/**
 * 判断字符串是否不为空且不为空白
 */
fun String?.isNotEmptyOrBlank(): Boolean {
    return !this.isNullOrBlank()
}

/**
 * 安全截取字符串
 * 防止 IndexOutOfBoundsException
 * @param maxLength 最大长度
 */
fun String.safeSubstring(maxLength: Int): String {
    return if (this.length <= maxLength) this else this.substring(0, maxLength)
}

/**
 * 手机号脱敏（中间4位替换为****）
 * 例如：13812345678 → 138****5678
 */
fun String.maskPhone(): String {
    if (this.length != 11) return this
    return this.replaceRange(3, 7, "****")
}