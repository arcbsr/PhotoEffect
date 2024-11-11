package com.crop.phototocartooneffect.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.utils.RLog;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    final PermissionAccess permissionAccess = new PermissionAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RLog.e("Rafiur>>>onCreate");
        findViewById(R.id.btnGrantPermission).setOnClickListener(v -> permissionAccess.requestStoragePermission(MainActivity.this));

    }

    private void checkPermission() {
        permissionAccess.checkRequestStoragePermission(this, new PermissionAccess.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                startActivity(new Intent(MainActivity.this, ImageAiActivity.class));
                finish();
            }


            @Override
            public void onPermissionDenied() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }
}