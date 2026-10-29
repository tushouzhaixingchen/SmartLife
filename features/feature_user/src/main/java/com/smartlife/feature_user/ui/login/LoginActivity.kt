package com.smartlife.feature_user.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartlife.feature_user.databinding.ActivityLoginBinding
import com.smartlife.feature_user.ui.register.RegisterActivity
import com.smartlife.library_common.auth.AuthProviderRegistry
import com.smartlife.library_common.auth.AuthType
import com.smartlife.library_common.auth.LoginGuard
import com.smartlife.library_common.base.BaseActivity
import com.smartlife.library_common.ext.clickDebounce
import com.smartlife.library_common.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.smartlife.library_common.core.UiState

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val vm: LoginViewModel by viewModels()
    private var currentType = AuthType.PHONE_SMS

    override fun getViewBinding(inflater: LayoutInflater) = ActivityLoginBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lucas", "LoginActivity onCreate")
        renderLoginTypes()                                   // 按启用的方式渲染 Tab/按钮
        binding.btnSendCode.clickDebounce { vm.sendCode(currentType, binding.etPhone.text.toString()) }
        binding.btnLogin.clickDebounce { vm.login(currentType, binding.etPhone.text.toString(), binding.etCode.text.toString()) }
        binding.tvGoRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }

        observeState()
        observeCountdown()
    }

    // 关键：入口"长什么样"由配置决定，切登录方式=改 LoginTypeConfig.allowedTypes
    private fun renderLoginTypes() {
        val list = AuthProviderRegistry.available().map { it.type }
        currentType = list.firstOrNull() ?: AuthType.PHONE_SMS
        binding.tabLoginType.isVisible = list.size > 1          // 只有一种时隐藏切换条
    }

    // 监听 UI 状态
    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { s ->
                    when (s) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            onLoginSuccess()
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            ToastUtils.showShort(s.message)
                        }
                        is UiState.Loading -> {
                            binding.progressBar.visibility = android.view.View.VISIBLE
                        }
                        is UiState.Empty -> {
                            binding.progressBar.visibility = android.view.View.GONE
                        }
                    }
                }
            }
        }
    }

    // 监听验证码倒计时
    private fun observeCountdown() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.countdown.collect { sec ->
                    binding.btnSendCode.isEnabled = sec == 0
                    binding.btnSendCode.text = if(sec == 0) "获取验证码" else "${sec}s"
                }
            }
        }
    }

    // 登录成功后的处理逻辑
    private fun onLoginSuccess() {
        // 1. 尝试续跑被拦截的动作（如果有的话）
        LoginGuard.runPendingAction()

        // 2. 跳转回主页
        val intent = Intent().setClassName(
            packageName,
            "com.smartlife.app.MainActivity" // 确保包名与你的 MainActivity 一致
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)

        // 3. 销毁当前登录页
        finish()
    }
}