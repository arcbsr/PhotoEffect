package com.crop.phototocartooneffect.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.activities.MainActivity;
import com.crop.phototocartooneffect.activities.PermissionAccess;

public class PermissionFragment extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1001;
//    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String[] getRequiredPermissions() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                return permissions;
            } else {
                return new String[0];
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    String[] dynamicPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dynamicPermissions = getRequiredPermissions();

        findViewById(R.id.btnGrantPermission).setOnClickListener(v -> {
            if (checkPermissions()) {
                onPermissionsGranted();
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                requestPermissions();
            }
        });
        if (checkPermissions()) {
            onPermissionsGranted();
        } else {

        }
    }

    public boolean checkRequestStoragePermission() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // For Android 11 and above, MANAGE_EXTERNAL_STORAGE is required
//            if (!Environment.isExternalStorageManager()) {
//                return false;
//            } else {
//                return true;
//            }
//        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkPermissions() {
//        for (String permission : dynamicPermissions) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
        return checkRequestStoragePermission();
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, dynamicPermissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                // Permissions granted, proceed with your logic
                onPermissionsGranted();
            } else {
                // Permissions denied, show explanation or disable functionality
                onPermissionsDenied();
            }
        }
    }

    private void onPermissionsGranted() {
        // Implement your logic when permissions are granted
        startActivity(new Intent(PermissionFragment.this, ImageAiActivity.class));
        finish();
    }

    private void onPermissionsDenied() {
        // Implement your logic when permissions are denied
    }

}