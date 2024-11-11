package com.crop.phototocartooneffect.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.permissionx.guolindev.PermissionX;

public class PermissionAccess {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private PermissionCallback permissionCallback;

    public interface PermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied();
    }

    public void checkRequestStoragePermission(Activity activity, PermissionCallback callback) {
        this.permissionCallback = callback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 and above, MANAGE_EXTERNAL_STORAGE is required
            if (!Environment.isExternalStorageManager()) {
                permissionCallback.onPermissionDenied();
            } else {
                permissionCallback.onPermissionGranted();
            }
            return;
        }
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionCallback.onPermissionDenied();
        } else {
            permissionCallback.onPermissionGranted();
        }
    }

    public void requestStoragePermission(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 and above, MANAGE_EXTERNAL_STORAGE is required
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivity(intent);
            } else {
                permissionCallback.onPermissionGranted();
            }
        } else {
            PermissionX.init(activity).permissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request((allGranted, grantedList, deniedList) -> {
                        if (allGranted) {
                            permissionCallback.onPermissionGranted();
                        } else {
                            permissionCallback.onPermissionDenied();
                        }
                    });
        }
    }

    public void checkRequestCameraPermission(Activity activity, PermissionCallback callback) {
        this.permissionCallback = callback;
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionCallback.onPermissionDenied();
        } else {
            permissionCallback.onPermissionGranted();
        }
    }

    public void requestCameraPermission(AppCompatActivity activity, PermissionCallback callback) {
        PermissionX.init(activity).permissions(android.Manifest.permission.CAMERA)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        callback.onPermissionGranted();
                    } else {
                        callback.onPermissionDenied();
                    }
                });
    }

}
