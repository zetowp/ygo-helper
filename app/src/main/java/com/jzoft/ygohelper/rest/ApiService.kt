package com.jzoft.ygohelper.rest

import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * Created by jjimenez on 7/08/17.
 */
interface ApiService {

    @POST
    @FormUrlEncoded
    fun auth(@Url url: String, @Field("username") username: String,
             @Field("password") password: String, @Field("grant_type") grant: String = "password"): Observable<Token>

    @GET
    fun getFromWeb(@Url url: String): Observable<Response<ByteArray>>

}