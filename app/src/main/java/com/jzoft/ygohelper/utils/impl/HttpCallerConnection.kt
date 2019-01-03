package com.jzoft.ygohelper.utils.impl

import com.jzoft.ygohelper.utils.HttpCaller

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

/**
 * Created by jjimenez on 13/10/16.
 */
class HttpCallerConnection : HttpCaller {

    @Throws(HttpCaller.NotFound::class)
    override fun getCall(url: String): ByteArray {
        try {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            evaluateResponse(urlConnection, url)
            return getBytes(urlConnection.inputStream)
        } catch (e: UnknownHostException) {
            throw HttpCaller.NotFound(url)
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }

    }

    @Throws(IOException::class, HttpCaller.NotFound::class)
    private fun evaluateResponse(urlConnection: HttpURLConnection, location: String) {
        if (urlConnection.responseCode == 404) {
            throw HttpCaller.NotFound(location)
        }
    }

    @Throws(IOException::class)
    private fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = inputStream.read(buffer)
        while (len  != -1) {
            byteBuffer.write(buffer, 0, len)
            len = inputStream.read(buffer)
        }
        inputStream.close()
        return byteBuffer.toByteArray()
    }
}
