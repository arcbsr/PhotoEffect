package com.crop.phototocartooneffect.utils;

import android.content.Context;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.models.MenuItem;

public class AppSettings {
    public static final MenuItem DEFAULT_ITEM = new MenuItem(R.drawable.pro_icon_24, "Remove Background", "", R.drawable.thumb, EditingCategories.ImageCreationType.FIREBASE_ML_SEGMENTATION);
    public static final int ANIMATION_DURATION = 4000;
    public static final int IMAGE_PLACE_HOLDER = R.drawable.placeholder;
    public static final int IMAGE_PLACE_HOLDER_ERROR = R.drawable.error_image;
    public static final int IMAGE_PLACE_HOLDER_BANNER = R.drawable.thumb2;
    public static final int IMAGE_PLACE_HOLDER_ERROR_BANNER = R.drawable.thumb2;

    public static final boolean IS_TESTING_MODE = false;
    public static final boolean IS_TESTING_MODE_MINOR = true;
    public static final boolean IS_ADMIN_MODE = true;

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}
