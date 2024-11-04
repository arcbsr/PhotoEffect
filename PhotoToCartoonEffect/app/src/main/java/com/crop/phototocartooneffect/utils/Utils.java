package com.crop.phototocartooneffect.utils;

import android.content.res.Resources;

public class Utils {

    public static int getDevicePixelWidth(int size) {
        return (int) (size * Resources.getSystem().getDisplayMetrics().density);
    }
}
