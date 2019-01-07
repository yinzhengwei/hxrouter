package com.yzw.hxrouter

/**
 * Create by yinzhengwei on 2019/1/7
 * @Function 跳转时的拦截器
 */
interface HxInterceptor {
    fun process(interceptorResult: HxInterceptorResult)
}

interface HxInterceptorResult {
    fun finish()
    fun cancel()
}