package com.smartlife.library_common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = getViewBinding(layoutInflater)
        setContentView(binding.root)
    }

    /*子类实现此方法，返回对应的ViewBinding
    * 例如：return ActivityMainBinding.inflate(inflater)*/
    abstract fun getViewBinding(inflater: LayoutInflater): VB
}