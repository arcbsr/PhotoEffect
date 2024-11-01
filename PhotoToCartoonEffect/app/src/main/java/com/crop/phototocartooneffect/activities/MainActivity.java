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
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // For Android 11 and above, MANAGE_EXTERNAL_STORAGE is required
//            if (!Environment.isExternalStorageManager()) {
//                // Request permission
//                findViewById(R.id.btnGrantPermission).setOnClickListener(v -> {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    startActivity(intent);
//                });
//            } else {
//
//            }
//        } else {
//            PermissionX.init(this).permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .request((allGranted, grantedList, deniedList) -> {
//                        if (allGranted) {
//                            finish();
//                        } else {
//                            findViewById(R.id.btnGrantPermission).setOnClickListener(v -> {
//                                requestPermissions();
//                            });
//                        }
//                    });
//        }
//
//
//    }

//    private void requestPermissions() {
//        // Check if the permission has already been granted
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // Check if we should show an explanation to the user
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
//                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                // Show explanation dialog and request permissions again
//                new AlertDialog.Builder(this)
//                        .setTitle(getString(R.string.storage_permission_title))
//                        .setMessage(getString(R.string.storage_permission_description))
//                        .setPositiveButton("Allow", (dialog, which) -> {
//                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
//                                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            }, REQUEST_PERMISSIONS);
//                        })
//                        .setNegativeButton("Deny", null)
//                        .create()
//                        .show();
//
//            } else {
//                // Request permissions for the first time or when "Don't ask again" was selected
//                ActivityCompat.requestPermissions(this, new String[]{
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                }, REQUEST_PERMISSIONS);
//            }
//        } else {
//
//        }
//    }
}