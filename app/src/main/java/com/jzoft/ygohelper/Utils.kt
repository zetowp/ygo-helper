package com.jzoft.ygohelper

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun View.visibleOrGone(visible: Boolean) {
    if (visible) visible() else gone()
}

fun View.visibleOrNotVisible(visible: Boolean) {
    if (visible) visible() else invisible()
}


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadUrl(url: String, placeholder: Int = 0, error: Int = 0, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
    Glide.with(context).asBitmap().load(url)
            .apply(RequestOptions().placeholder(placeholder).error(error))
            .listener(buildListener(onSuccess, onError))
            .into(this)
}

fun buildListener(onSuccess: () -> Unit, onError: () -> Unit): RequestListener<Bitmap> = object : RequestListener<Bitmap> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
        onError.invoke()
        return false
    }

    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        onSuccess.invoke()
        return false
    }

}