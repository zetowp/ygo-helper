package com.jzoft.ygohelper.ui


class Option(val id: Int?, val name: String?, val iconId: Int, val onClick: () -> Unit) {
    companion object {
        val PROXY = 1
        val CALCULATOR = 2
    }
}
