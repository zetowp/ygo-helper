package com.jzoft.ygohelper;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.jzoft.ygohelper.adapters.PatchAdapter;
import com.jzoft.ygohelper.databinding.ActivityPatchCreatorBinding;
import com.jzoft.ygohelper.utils.ActivityUtils;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ProxyCardCreatorFragment extends YgoFragment {


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String INFO_PROXY = "Proxy Maker Instructions:\n" +
            "On the image:\n" +
            "- the paste you can paste an allready copied url from wikia of the card you want to make a proxy, or the url of the image.\n" +
            "- copy will create a copy of the proxy\n" +
            "- X To delete\n" +
            "\n" +
            "Menu\n" +
            "- Trash can will delete all of your proxys\n" +
            "- Print will create a html that you can send to yous computer (e.g. via Email), open it with Chrome or other browser and print it.\n";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(getActivity());
        ActivityPatchCreatorBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.activity_patch_creator, container, false);
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        patchAdapter = new PatchAdapter((ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE), keyboard, getActivity());
        binding.patchList.setAdapter(patchAdapter);
        return binding.getRoot();
    }

    public List<Integer> getOptions() {
        return Arrays.asList(R.id.print_patches, R.id.clear, R.id.info);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.print_patches:
                patchAdapter.print();
                break;
            case R.id.clear:
                ActivityUtils.okSimpleCancelAlert(createCleearAlertHolder(), getOkClearAction());
                break;
            case R.id.info:
                ActivityUtils.simpleAlert(getActivity(), INFO_PROXY);
            default:
                break;
        }
        return true;
    }

    @NonNull
    private ActivityUtils.AlertHolder createCleearAlertHolder() {
        return new ActivityUtils.AlertHolder("Do you really want to clear all the proxys?",
                "Delete it!", "Not this time", getActivity());
    }

    @NonNull
    private DialogInterface.OnClickListener getOkClearAction() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                patchAdapter.clear();
                dialog.dismiss();
            }
        };
    }


    @Override
    public String getTitle() {
        return "Proxy Maker";
    }
}
