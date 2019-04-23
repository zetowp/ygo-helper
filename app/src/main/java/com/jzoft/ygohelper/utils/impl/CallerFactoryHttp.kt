package com.jzoft.ygohelper.utils.impl

import com.jzoft.ygohelper.utils.Caller
import com.jzoft.ygohelper.utils.CallerFactory

/**
 * Created by jjimenez on 14/10/16.
 */
class CallerFactoryHttp : CallerFactory {
    override fun createCaller(): Caller {
        return CallerHttpConnection()
    }
}
