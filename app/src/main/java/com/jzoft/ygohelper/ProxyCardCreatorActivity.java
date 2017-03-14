package com.jzoft.ygohelper;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.jzoft.ygohelper.adapters.PatchAdapter;
import com.jzoft.ygohelper.databinding.ActivityPatchCreatorBinding;

public class ProxyCardCreatorActivity extends BaseActivity {


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private DrawerLayout tools;

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private PatchAdapter patchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        ActivityPatchCreatorBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patch_creator);
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        patchAdapter = new PatchAdapter((ClipboardManager) getSystemService(CLIPBOARD_SERVICE), keyboard, this);
        binding.patchList.setAdapter(patchAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.patches_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.print_patches:
                patchAdapter.print();
                break;
            case R.id.clear:
                askToClear();
                break;
            default:
                break;
        }
        return true;
    }

    private void askToClear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to clear all the proxys?");
        builder.setPositiveButton("Delete it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                patchAdapter.clear();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Not this time", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
