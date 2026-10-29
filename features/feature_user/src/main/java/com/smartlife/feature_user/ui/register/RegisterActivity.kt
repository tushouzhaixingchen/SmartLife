package com.smartlife.feature_user.ui.register

import android.os.Bundle
import com.smartlife.feature_user.databinding.ActivityRegisterBinding
import com.smartlife.library_common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    override fun getViewBinding(inflater: android.view.LayoutInflater): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: 注册逻辑（跟登录页类似，调 ViewModel）
        binding.btnRegister.setOnClickListener {
            // 注册成功后结束当前页面
            finish()
        }
    }
}