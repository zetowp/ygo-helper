package com.focaltec.gastosdeviaje.gastosdeviajeapp.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.jzoft.ygohelper.rest.ApiService
import com.jzoft.ygohelper.rest.Token
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * Created by jjimenez on 7/08/17.
 */
class RetrofitClient() {

    private var authToken: Token? = null

    fun apiService(baseUrl: String? = null): ApiService {
        val httpClient = initClient()
        val builder = initBuilder(baseUrl)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        if (authToken != null) httpClient.addInterceptor(header(authToken!!))
        builder.client(httpClient.build())
        return builder.build().create(ApiService::class.java)
    }

    private fun initClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
    }

    private fun initBuilder(baseUrl: String?): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(buildMapper()))
    }

    fun buildMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper
    }

    private fun header(token: Token): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response {
                val request = chain!!.request().newBuilder()
                val build = request.addHeader("authorization", "${token.tokenType} ${token.accessToken}")
                        .build()
                return chain.proceed(build);
            }
        }
    }


    fun doLogin(urlAuth: String, user: String, password: String): Observable<Token> {
        return apiService().auth(urlAuth, user, password).flatMap {
            authToken = it
            Observable.just(it)
        }
    }

}
