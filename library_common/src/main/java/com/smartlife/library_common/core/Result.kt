package com.smartlife.library_common.core

/*
* 统一的数据结果包装类
* 用于表示依次数据请求的三种状态：成功、失败、加载中
* 使用sealed密封类表示仅有三种状态，当when的时候不用写else，编译器知道只有三种状态
* out=协变（Covariance），T只能出现在输出位置，协变表示子类之间可以替换父类
* T是Type类型的缩写，T的意思是泛型类型，代表某种类型是一个占位符，没有具体含义，表示Result类可以包含任意类型的数据
* Java/Kotlin/C#等语言中，T=Type、E=Element、V=Value、R=Result、K=Key
* Nothing是Kotlin中的一个特殊类型，表示空类型，表示没有值
* */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : Result<Nothing>()

    object Loading : Result<Nothing>()
}