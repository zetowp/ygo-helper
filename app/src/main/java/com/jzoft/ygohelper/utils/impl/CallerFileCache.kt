package com.jzoft.ygohelper.utils.impl

import com.jzoft.ygohelper.utils.Caller
import rx.Observable
import java.io.File

/**
 * Created by jjimenez on 14/10/16.
 */
class CallerFileCache(val caller: Caller, val baseDirectory: File) : Caller {

    init {
        if (!baseDirectory.exists()) baseDirectory.mkdirs()
    }

    override fun getCall(url: String): Observable<ByteArray> {
        val filename = "proxy_${nameFomUrl(url)}"
        val image = getBytesIfExist(filename)
        return if (image != null) Observable.just(image)
        else caller.getCall(url).flatMap {
            File(baseDirectory, filename).writeBytes(it)
            Observable.just(it)
        }
    }

    private fun getBytesIfExist(filename: String): ByteArray? {
        val file = File(baseDirectory, filename)
        return if (file.exists()) file.readBytes()
        else null
    }

    private fun nameFomUrl(url: String): String {
        val sufix = getSufix(url)
        return (if (sufix != null) url.substringBeforeLast(sufix) else url).substringAfterLast("/") + (sufix
                ?: "")
    }

    private fun getSufix(url: String): String? {
        return when {
            url.indexOf(".png", 0, false) != -1 -> ".png"
            url.indexOf(".jpg", 0, false) != -1 -> ".jpg"
            url.indexOf(".jpeg", 0, false) != -1 -> ".jpeg"
            url.indexOf(".gif", 0, false) != -1 -> ".gif"
            url.indexOf(".bmp", 0, false) != -1 -> ".bmp"
            else -> null
        }
    }
}
