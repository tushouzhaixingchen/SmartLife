package com.smartlife.library_common.auth

enum class AuthType(val code:String, val label:String) {
    PHONE_SMS("phone_sms","手机验证码"),
    ACCOUNT_PWD("account_pwd","账号密码"),
    EMAIL_CODE("email_code","邮箱验证码"),
    WECHAT("wechat","微信"),
    QQ("qq","QQ")
}
