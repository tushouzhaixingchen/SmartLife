package com.smartlife.library_common.ext

import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Fragment 扩展函数
 */

/**
 * 快速显示 Toast
 */
fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toastLong(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}