package com.jzoft.ygohelper.utils.impl

import android.os.AsyncTask
import com.jzoft.ygohelper.utils.Caller
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

/**
 * Created by jjimenez on 11/10/16.
 */
class CallerHttpConnection :Caller {

    @Throws(Caller.NotFound::class)
    override fun getCall(url: String): Observable<ByteArray> {
        return Observable.fromCallable { getHttp(url) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getHttp(url: String): ByteArray {
        try {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            evaluateResponse(urlConnection, url)
            return getBytes(urlConnection.inputStream)
        } catch (e: UnknownHostException) {
            throw Caller.NotFound(url)
        } catch (e: IOException) {
            throw Caller.NotFound(url)
        }
    }

    @Throws(IOException::class, Caller.NotFound::class)
    private fun evaluateResponse(urlConnection: HttpURLConnection, location: String) {
        if (urlConnection.responseCode == 404) {
            throw Caller.NotFound(location)
        }
    }

    @Throws(IOException::class)
    private fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = inputStream.read(buffer)
        while (len != -1) {
            byteBuffer.write(buffer, 0, len)
            len = inputStream.read(buffer)
        }
        inputStream.close()
        return byteBuffer.toByteArray()
    }
}
