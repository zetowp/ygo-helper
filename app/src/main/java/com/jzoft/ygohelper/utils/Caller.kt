package com.jzoft.ygohelper.utils

import rx.Observable

/**
 * Created by jjimenez on 13/10/16.
 */
interface Caller {

    @Throws(Caller.NotFound::class)
    fun getCall(url: String): Observable<ByteArray>


    class NotFound(val url: String) : Exception()
}
