package com.jzoft.ygohelper.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

import com.jzoft.ygohelper.R

/**
 * Created by jjimenez on 10/03/17.
 */
object ActivityUtils {
    fun simpleAlert(ctx: Context, message: String) {
        val builder = AlertDialog.Builder(ctx, R.style.NormalAlert)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    fun okSimpleCancelAlert(holder: AlertHolder, okAction: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(holder.ctx, R.style.NormalAlert)
        builder.setMessage(holder.message)
        builder.setPositiveButton(holder.ok, okAction)
        builder.setNegativeButton(holder.cancel) { _, _ -> }
        builder.create().show()
    }


    class AlertHolder(internal var message: String, internal var ok: String, internal var cancel: String, internal var ctx: Context)
}
