package com.crop.phototocartooneffect.animations;
// DepthPageTransformer.java

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class DepthPageTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            page.setAlpha(0f);
        } else if (position <= 0) {
            page.setAlpha(1f);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (position <= 1) {
            page.setAlpha(1 - position);
            page.setTranslationX(page.getWidth() * -position);
            float scaleFactor = 0.75f + (1 - 0.75f) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else {
            page.setAlpha(0f);
        }
    }
}
