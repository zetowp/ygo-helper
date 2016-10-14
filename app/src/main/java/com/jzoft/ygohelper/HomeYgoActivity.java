package com.jzoft.ygohelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HomeYgoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ygo);
        autoRedirect();
    }

    private void autoRedirect() {
        startActivity(new Intent(this, PatchCreatorActivity.class));
    }
}
