package com.smartlife.library_common.auth

import android.content.Context
import android.content.Intent
import android.util.Log

object LoginLauncher {
    object Constants {
        const val TAG = "MainActivity"
    }
    fun launchLogin(ctx: Context) {
        Log.d("Lucas", "Launching login activity")
        ctx.startActivity(Intent().setClassName(ctx.packageName,
            "com.smartlife.feature_user.ui.login.LoginActivity"))
    }
}