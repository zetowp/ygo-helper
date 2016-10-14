package com.jzoft.ygohelper;

import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.jzoft.ygohelper.adapters.PatchAdapter;
import com.jzoft.ygohelper.databinding.ActivityPatchCreatorBinding;

public class PatchCreatorActivity extends AppCompatActivity {


    private PatchAdapter patchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            default:
                break;
        }
        return true;
    }
}
