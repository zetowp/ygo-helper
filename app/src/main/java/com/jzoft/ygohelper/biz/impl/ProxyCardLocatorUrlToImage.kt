package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.utils.Caller
import com.jzoft.ygohelper.utils.CallerFactory
import com.jzoft.ygohelper.utils.ImageOptimizer
import rx.Observable

/**
 * Created by jjimenez on 13/10/16.
 */
class ProxyCardLocatorUrlToImage(private val caller: Caller) : ProxyCardLocator {

    @Throws(Caller.NotFound::class)
    override fun locate(location: String): Observable<ProxyCard> {
        return caller.getCall(location).flatMap { Observable.just(ProxyCard(location, it)) }
    }

    override fun hasToLocate(location: String): Boolean {
        return true
    }
}
