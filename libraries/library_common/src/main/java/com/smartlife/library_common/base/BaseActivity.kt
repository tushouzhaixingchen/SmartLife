package com.smartlife.library_common.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        // 📌 全局统一强制竖屏，所有子类自动生效
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = getViewBinding(layoutInflater)
        setContentView(binding.root)
    }

    /*子类实现此方法，返回对应的ViewBinding
    * 例如：return ActivityMainBinding.inflate(inflater)*/
    abstract fun getViewBinding(inflater: LayoutInflater): VB
}