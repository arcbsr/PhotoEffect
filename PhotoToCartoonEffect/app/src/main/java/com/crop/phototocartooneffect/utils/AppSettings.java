package com.crop.phototocartooneffect.utils;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.models.MenuItem;

public class AppSettings {
    public static final MenuItem DEFAULT_ITEM = new MenuItem(R.drawable.thumb, "Remove Background", "", R.drawable.thumb4, ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION);
}
