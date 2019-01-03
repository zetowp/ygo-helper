package com.jzoft.ygohelper.utils

import com.jzoft.ygohelper.biz.impl.GetCallAsync

/**
 * Created by jjimenez on 14/10/16.
 */
class HttpCallerFactory {

    val caller: HttpCaller
        get() = GetCallAsync()
}
