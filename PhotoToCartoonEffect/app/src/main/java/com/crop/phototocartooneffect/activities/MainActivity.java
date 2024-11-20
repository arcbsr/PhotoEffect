package com.crop.phototocartooneffect.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crop.phototocartooneffect.BuildConfig;
import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.firabsehelper.AnalyticsHelper;
import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.repositories.AppResources;
import com.crop.phototocartooneffect.utils.RLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    final PermissionAccess permissionAccess = new PermissionAccess();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean IS_AUTH_CHECKING_DONE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IS_AUTH_CHECKING_DONE = false;
        authFirebase();

    }

    private void authFirebase() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Authenticate user anonymously
        if (mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
//                    initializeUserCoins(user);
                    checkAppVersion(user);
                    RLog.e("Authentication Added successfully.");
                } else {
                    askToDownloadNewVersion(-1);
                    RLog.e("MainActivity", "Authentication failed", task.getException().getMessage());
                }
            });
        } else {
            // User already signed in anonymously
            FirebaseUser user = mAuth.getCurrentUser();
//            initializeUserCoins(user);
            checkAppVersion(user);
        }
    }

    private void checkAppVersion(FirebaseUser user) {
        if (user != null) {
            String userId = user.getUid();

            db.collection("appsettings").document("settings").get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    long version = documentSnapshot.getLong("version");
                    RLog.e("MainActivity", "Version: " + version);
                    askToDownloadNewVersion(version);
                }
            }).addOnFailureListener(e -> {
                // Handle failure
                askToDownloadNewVersion(-1);
            });
        }
    }

    private void askToDownloadNewVersion(long version) {
        int currentVersion = BuildConfig.VERSION_CODE;
        AnalyticsHelper.getInstance(MainActivity.this).
                logEvent("startapp", "v-" + version + ":" + currentVersion);
        if (version > currentVersion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update Available").setMessage("A new version of the app is available. Would you like to update?").setPositiveButton("Update", (dialog, which) -> {
                // Open Play Store or your app's download page
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } finally {
                    dialog.dismiss();
                    finish();
                }
            }).setNegativeButton("Later", (dialog, which) -> {
                dialog.dismiss();
                initAPP();
            }).setCancelable(false).show();
        } else {
            initAPP();
        }
    }


    private SharedPreferences preferences;


    private void initAPP() {
        AppResources.getInstance().reFreshAppItems(this, () -> {
            IS_AUTH_CHECKING_DONE = true;
            findViewById(R.id.btnGrantPermission).setOnClickListener(v -> permissionAccess.requestStoragePermission(MainActivity.this));
            checkPermission();
        });
    }

    private void checkPermission() {
        permissionAccess.checkRequestStoragePermission(this, new PermissionAccess.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                AnalyticsHelper.getInstance(MainActivity.this).
                        logEvent("startapp", "permission_granted");
                startActivity(new Intent(MainActivity.this, ImageAiActivity.class));
                finish();
            }


            @Override
            public void onPermissionDenied() {
                findViewById(R.id.permission_view).setVisibility(View.VISIBLE);
                findViewById(R.id.loading_view).setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_AUTH_CHECKING_DONE) {
            checkPermission();
        }
    }
}