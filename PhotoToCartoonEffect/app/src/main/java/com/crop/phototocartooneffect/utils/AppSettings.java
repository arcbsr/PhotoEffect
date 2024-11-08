package com.crop.phototocartooneffect.utils;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.models.MenuItem;

public class AppSettings {
    public static final MenuItem DEFAULT_ITEM = new MenuItem(R.drawable.pro_icon_24, "Remove Background", "", R.drawable.thumb, ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION);
    public static final int ANIMATION_DURATION = 4000;
}
