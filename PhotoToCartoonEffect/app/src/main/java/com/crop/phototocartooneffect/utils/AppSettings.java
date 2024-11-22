package com.crop.phototocartooneffect.utils;

import android.content.Context;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.models.MenuItem;

public class AppSettings {
    public static final MenuItem DEFAULT_ITEM = new MenuItem(R.drawable.pro_icon_24, "Remove Background", "", R.drawable.thumb, EditingCategories.ImageCreationType.FIREBASE_ML_SEGMENTATION);
    public static final int ANIMATION_DURATION = 4000;
    public static final int IMAGE_PLACE_HOLDER = R.drawable.thumb;
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

    private static final int[] ImagePlace_Holder = {R.drawable.thumb, R.drawable.thumb3, R.drawable.thumb4, R.drawable.thumb5, R.drawable.thumb6};
    private static final int[] ImagePlaceBanner_Holder = {R.drawable.thumb2};

    public static MenuItem getRandomItem() {
        MenuItem item = new MenuItem(R.drawable.pro_icon_24, "Remove Background", "", R.drawable.thumb, EditingCategories.ImageCreationType.values()[(int) (Math.random() * EditingCategories.ImageCreationType.values().length)]);
        item.imageUrl = null;
        item.setThumbResId(ImagePlace_Holder[(int) (Math.random() * ImagePlace_Holder.length)]);
        item.prompt = "Transform this image into a surreal, dreamlike scene with glowing, vibrant colors.";
        return item;
    }

    public static MenuItem getRandomBannerItem() {
        MenuItem item = new MenuItem(R.drawable.pro_icon_24, "Remove Background", "", R.drawable.thumb2, EditingCategories.ImageCreationType.values()[(int) (Math.random() * EditingCategories.ImageCreationType.values().length)]);
        item.imageUrl = null;
        item.setThumbResId(ImagePlace_Holder[(int) (Math.random() * ImagePlaceBanner_Holder.length)]);
        item.prompt = "Transform this image into a surreal, dreamlike scene with glowing, vibrant colors.";
        return item;
    }
}
