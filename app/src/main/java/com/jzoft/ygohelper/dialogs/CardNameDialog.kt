package com.jzoft.ygohelper.dialogs

import android.support.v4.app.DialogFragment
import com.jzoft.ygohelper.R
import kotlinx.android.synthetic.main.dialog_write_name.*

class CardNameDialog : BaseDialog() {

    override val layout = R.layout.dialog_write_name
    private var onOk: (String) -> Unit = {}

    companion object {

        fun build(onOk: (String) -> Unit): CardNameDialog {
            val d = CardNameDialog()
            d.onOk = onOk
            return d
        }
    }


    override fun initView() {
        disableCloseOnClickOutside()
        close.setOnClickListener { dismiss() }
        cancel.setOnClickListener { dismiss() }
        ok.setOnClickListener {
            val result = value.text.toString()
            if (result.isNotBlank()) onOk.invoke(result)
            dismiss()
        }
    }

}