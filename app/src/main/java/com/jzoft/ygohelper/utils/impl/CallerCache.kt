package com.jzoft.ygohelper.utils.impl

import com.jzoft.ygohelper.utils.Caller
import com.jzoft.ygohelper.utils.CallerFactory
import rx.Observable

/**
 * Created by jjimenez on 14/10/16.
 */
class CallerCache(val caller: Caller) : Caller {

    companion object{
        val cache:  MutableMap<String, ByteArray> = mutableMapOf()
    }
    override fun getCall(url: String): Observable<ByteArray> {
        return if (cache[url] != null) Observable.just(cache[url])
        else caller.getCall(url).flatMap {
            cache[url] = it
            Observable.just(cache[url])
        }
    }
}
