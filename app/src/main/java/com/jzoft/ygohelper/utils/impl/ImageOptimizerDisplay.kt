package com.jzoft.ygohelper.utils.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jzoft.ygohelper.utils.Caller

import com.jzoft.ygohelper.utils.ImageOptimizer
import rx.Observable

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Created by jjimenez on 13/10/16.
 */
class ImageOptimizerDisplay(val caller: Caller) : ImageOptimizer, Caller {

    override fun getCall(url: String): Observable<ByteArray> {
        return caller.getCall(url).flatMap { Observable.just(optimizeImage(it)) }
    }

    override fun optimizeImage(image: ByteArray): ByteArray {
        val bitmap = scaleDown(BitmapFactory.decodeStream(ByteArrayInputStream(image)), 120f, true)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    companion object {

        fun scaleDown(realImage: Bitmap, widthExpected: Float, filter: Boolean): Bitmap {
            val ratio = widthExpected / realImage.width
            val width = Math.round(ratio * realImage.width)
            val height = Math.round(ratio * realImage.height)
            return Bitmap.createScaledBitmap(realImage, width,
                    height, filter)
        }
    }
}
