package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.utils.HttpCaller
import com.jzoft.ygohelper.utils.HttpCallerFactory
import com.jzoft.ygohelper.utils.ImageOptimizer

/**
 * Created by jjimenez on 13/10/16.
 */
class ProxyCardLocatorUrlToImage(private val callerFactory: HttpCallerFactory, private val optimizer: ImageOptimizer) : ProxyCardLocator {

    @Throws(HttpCaller.NotFound::class)
    override fun locate(location: String): ProxyCard {
        return ProxyCard(location, optimizer.optimizeImage(callerFactory.caller.getCall(location)))
    }

    override fun hasToLocate(location: String): Boolean {
        return true
    }
}
