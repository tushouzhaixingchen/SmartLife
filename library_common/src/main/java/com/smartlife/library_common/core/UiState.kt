package com.smartlife.library_common.core

sealed class UiState<out T> {
    /*成功状态，携带数据*/
    data class Success<out T>(val data:T):UiState<T>()

    /*失败状态，携带错误信息*/
    data class Error(val message: String) : UiState<Nothing>()

    /*加载中状态*/
    object Loading : UiState<Nothing>()

    /*数据为空状态（如列表无数据时展示空页面*/
    object Empty : UiState<Nothing>()
}