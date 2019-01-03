package com.jzoft.ygohelper.biz.impl

import android.os.AsyncTask

import com.jzoft.ygohelper.utils.HttpCaller
import com.jzoft.ygohelper.utils.impl.HttpCallerConnection

import java.util.concurrent.ExecutionException

/**
 * Created by jjimenez on 11/10/16.
 */
class GetCallAsync : AsyncTask<String, Void, ByteArray>(), HttpCaller {

    private val caller: HttpCallerConnection
    private var notFound: HttpCaller.NotFound? = null

    init {
        caller = HttpCallerConnection()
    }

    override fun doInBackground(vararg strings: String): ByteArray? {
        try {
            return caller.getCall(strings[0])
        } catch (e: HttpCaller.NotFound) {
            notFound = e
            return null
        }

    }

    @Throws(HttpCaller.NotFound::class)
    override fun getCall(url: String): ByteArray {
        try {
            val bytes = execute(url).get()
            if (notFound != null) throw notFound!!
            return bytes
        } catch (e: InterruptedException) {
            throw IllegalStateException(e)
        } catch (e: ExecutionException) {
            throw IllegalStateException(e)
        }

    }
}
