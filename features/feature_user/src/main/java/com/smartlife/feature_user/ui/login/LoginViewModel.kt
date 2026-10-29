package com.smartlife.feature_user.ui.login

import com.smartlife.feature_user.data.repository.AuthRepository
import com.smartlife.library_common.auth.AuthSession
import com.smartlife.library_common.auth.AuthType
import com.smartlife.library_common.base.BaseViewModel
import com.smartlife.library_common.core.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val session: AuthSession) : LoginUiState
    data class Error(val msg: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository          // repo 由 Hilt 注入，无需自己定义
) : BaseViewModel() {

    private val _countdown = MutableStateFlow(0)
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    private val phoneRegex = Regex("^1[3-9]\\d{9}$")   // 规范7.3

    fun sendCode(type: AuthType, accountRaw: String) {
        val account = accountRaw.trim()
        if (type == AuthType.PHONE_SMS && !account.matches(phoneRegex)) {
            onError("手机号格式不正确")
            return                                   // 校验失败必须 return，否则会继续往下发请求
        }
        launch {                                     // 复用基类带异常处理的 launch
            when (val r = repo.sendCode(type, account)) {  // 自定义 sealed Result，用 when 匹配
                is Result.Success -> startCountdown()      // 发码成功只启动倒计时，不能置 Success 状态，否则会误触发登录跳转
                is Result.Error -> onError(r.message)
                is Result.Loading -> {}
            }
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            for (i in 60 downTo 1) {
                _countdown.value = i
                delay(1000)
            }
            _countdown.value = 0
        }
    }

    fun login(type: AuthType, phoneRaw: String, codeRaw: String) {
        val phone = phoneRaw.trim()
        if (type == AuthType.PHONE_SMS && !phone.matches(phoneRegex)) {
            onError("手机号格式不正确")
            return                                   // 这里同样要 return + 闭合括号
        }
        if (codeRaw.isBlank()) {
            onError("请输入验证码")
            return                                   // 这里也要 return
        }
        launch {
            onLoading()
            when (val r = repo.login(type, mapOf("phone" to phone, "code" to codeRaw.trim()))) {
                is Result.Success -> emitSuccess(r.data)     // 把 AuthSession 交给基类 emitSuccess
                is Result.Error -> onError(r.message)
                is Result.Loading -> {}
            }
        }
    }
}