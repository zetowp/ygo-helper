package com.jzoft.ygohelper

import android.view.View

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