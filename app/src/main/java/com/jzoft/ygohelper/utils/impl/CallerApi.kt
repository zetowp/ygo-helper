package com.jzoft.ygohelper.utils.impl

import com.jzoft.ygohelper.rest.ApiService
import com.jzoft.ygohelper.utils.Caller
import rx.Observable
import rx.Subscription

class CallerApi(val api: ApiService) : Caller {
    override fun getCall(url: String): Observable<ByteArray> {
        return api.getFromWeb(url).flatMap {
            if (it.isSuccessful) Observable.just(it.body()!!)
            else throw Caller.NotFound(url)
        }
    }

}