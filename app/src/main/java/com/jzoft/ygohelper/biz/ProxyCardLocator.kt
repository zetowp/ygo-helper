package com.jzoft.ygohelper.biz

import com.jzoft.ygohelper.utils.Caller
import rx.Observable


/**
 * Created by jjimenez on 11/10/16.
 */
interface ProxyCardLocator {

    fun locate(location: String): Observable<ProxyCard>

    fun hasToLocate(location: String): Boolean

}
