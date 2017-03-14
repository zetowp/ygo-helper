package com.jzoft.ygohelper.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.jzoft.ygohelper.R;

/**
 * Created by jjimenez on 10/03/17.
 */
public class ActivityUtils {
    public static void simpleAlert(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.NormalAlert);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void okSimpleCancelAlert(AlertHolder holder, DialogInterface.OnClickListener okAction){
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.activity, R.style.NormalAlert);
        builder.setMessage(holder.message);
        builder.setPositiveButton(holder.ok, okAction);
        builder.setNegativeButton(holder.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }


    public static final class AlertHolder {
        String message;
        String ok;
        String cancel;
        Activity activity;

        public AlertHolder(String message, String ok, String cancel, Activity activity) {
            this.message = message;
            this.ok = ok;
            this.cancel = cancel;
            this.activity = activity;
        }
    }
}
