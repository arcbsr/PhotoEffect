package com.crop.phototocartooneffect.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.utils.RLog;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RLog.e("Rafiur>>>onCreate");
        final PermissionAccess permissionAccess = new PermissionAccess();
        findViewById(R.id.btnGrantPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionAccess.requestStoragePermission(MainActivity.this);
            }
        });
        permissionAccess.checkRequestStoragePermission(this, new PermissionAccess.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ImageAiActivity.class));
                finish();
            }


            @Override
            public void onPermissionDenied() {

            }
        });
    }
}