package com.jzoft.ygohelper.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View


open class DialogFragmentNoClose : DialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
    }
}