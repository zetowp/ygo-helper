package com.jzoft.ygohelper.biz

import com.jzoft.ygohelper.utils.HttpCaller

/**
 * Created by jjimenez on 11/10/16.
 */
interface ProxyCardLocator {
    @Throws(HttpCaller.NotFound::class)
    fun locate(location: String): ProxyCard

    fun hasToLocate(location: String): Boolean

}
