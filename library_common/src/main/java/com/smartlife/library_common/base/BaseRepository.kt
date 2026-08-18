package com.smartlife.library_common.base

import android.util.Log.e
import com.smartlife.library_common.core.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

open class BaseRepository {
    /*安全的网络请求封装
    * 自动切换到IO线程，统一捕获异常*/
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T>{
        return withContext(Dispatchers.IO){
            try{
                Result.Success(apiCall())
            } catch (e: Exception){
                Result.Error(
                    message = e.message ?: "网络请求失败",
                    exception=e
                )
            }
        }
    }

    /*安全的数据库操作封装（单次操作，如insert、update、delete*/
    suspend fun <T> safeDbCall(dbCall: suspend() -> T): Result<T>{
        return withContext(Dispatchers.IO){
            try{
                Result.Success(dbCall())
            } catch (e: Exception){
                Result.Error(
                    message = e.message ?: "数据库操作失败",
                    exception = e
                )
            }
        }
    }

    /*安全的Flow封装（用于监听数据库变化
    * 自动切换到IO线程并捕获异常*/
    fun <T> safeFlow(flow: Flow<T>): Flow<Result<T>>{
        return flow
            .flowOn(Dispatchers.IO)
            /*
            * 为什么需要map？
            * 因为flow的类型是Flow<T>，而返回类型要求是Flow<Result<T>>
            * map的作用是将每个发射值T包装成Result.Success<T>，完成类型转换
            * 必须显式指定<T, Result<T>>，否则Kotlin会推断为Flow<Result.Success<T>>
            * 导致catch中emit(Result.Error)时类型不匹配
            * */
            .map<T,Result<T>> { Result.Success(it) }
            .catch { e ->
                emit(Result.Error(message = e.message ?: "数据流异常"))
            }
    }
}