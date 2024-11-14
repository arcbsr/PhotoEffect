package com.crop.phototocartooneffect.utils;

import android.content.res.Resources;

public class ScreenUtils {
    public static int getDevicePixelWidth(int size) {
        return (int) (size * Resources.getSystem().getDisplayMetrics().density);
    }
}
