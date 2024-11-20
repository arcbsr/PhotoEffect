package com.crop.phototocartooneffect.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class ScreenUtils {
    public static int getDevicePixelWidth(int size) {
        return (int) (size * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Bitmap createScaledBitmap(Bitmap bitmap, int width) {
        int height = (int) (bitmap.getHeight() * (400.0 / bitmap.getWidth()));
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
