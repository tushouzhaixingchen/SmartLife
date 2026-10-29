package com.smartlife.library_common.auth

object AuthProviderRegistry {
    private val strategies = linkedMapOf<AuthType, AuthStrategy>()
    fun register(s: AuthStrategy){ strategies[s.type]=s }
    fun get(t: AuthType) = strategies[t]
    fun available(): List<AuthStrategy> =
        strategies.values.filter { it.isEnabled() && LoginTypeConfig.isAllowed(it.type) } }