package com.jzoft.ygohelper.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by jjimenez on 11/09/17.
 */
class LoginClient(val url: String, val authToken: String) {


    fun doLogin(user: String, password: String): Observable<Token> {
        return apiService().auth(url + "token", user, password)
    }

    private fun apiService(): LoginService {
        val httpClient = initClient()
        val builder = initBuilder()
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.addInterceptor(header(authToken))
        builder.client(httpClient.build())
        return builder.build().create(LoginService::class.java)
    }

    private fun initClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
    }

    private fun initBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(buildMapper()))
    }

    private fun buildMapper(): ObjectMapper {
        val mapper = ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper
    }

    private fun header(token: String): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response {
                val request = chain!!.request();
                return chain.proceed(request.newBuilder()
                        .addHeader("authorization", "Basic  ${token}")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build());
            }
        }
    }
}

class Token(@JsonProperty("access_token") var accessToken: String? = null,
            @JsonProperty("token_type") var tokenType: String? = null,
            @JsonProperty("refresh_token") var refreshToken: String? = null)


interface LoginService {
    @POST
    @FormUrlEncoded
    fun auth(@Url url: String, @Field("username") username: String,
             @Field("password") password: String, @Field("grant_type") grant: String = "password"): Observable<Token>
}