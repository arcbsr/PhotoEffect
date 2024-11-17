package com.crop.phototocartooneffect.utils;

import android.content.Context;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.models.MenuItem;

public class AppSettings {
    public static final MenuItem DEFAULT_ITEM = new MenuItem(R.drawable.pro_icon_24, "Remove Background", "", R.drawable.thumb, ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION);
    public static final int ANIMATION_DURATION = 4000;

    public static final boolean IS_TESTING_MODE = false;
    public static final boolean IS_TESTING_MODE_MINOR = true;


    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
