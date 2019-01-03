package com.jzoft.ygohelper.utils

/**
 * Created by jjimenez on 13/10/16.
 */
interface HttpCaller {

    @Throws(HttpCaller.NotFound::class)
    fun getCall(url: String): ByteArray


    class NotFound(val url: String) : Exception()
}
