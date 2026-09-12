package com.smartlife.library_common.router

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Home 模块对外暴露的服务接口
 *
 * 放在 library_common 中，供其他模块依赖调用
 * 具体实现类放在 home 业务模块中
 */
interface IHomeService : IProvider {

    /**
     * 获取首页未读消息数量
     */
    fun getUnreadCount(): Int

    /**
     * 刷新首页数据
     */
    fun refreshHomeData()
}