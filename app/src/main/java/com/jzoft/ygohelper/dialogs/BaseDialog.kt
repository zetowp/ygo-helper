package com.jzoft.ygohelper.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseDialog : DialogFragment() {
    var onDismiss: () -> Unit = {}

    abstract val layout: Int
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layout, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun dismiss() {
        super.dismiss()
        onDismiss.invoke()
    }

    fun disableCloseOnClickOutside() {
        dialog.setCanceledOnTouchOutside(false)
    }

    abstract fun initView()
}