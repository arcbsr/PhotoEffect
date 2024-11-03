package com.crop.phototocartooneffect.renderengins;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public interface ImageEffect {
    interface ImageEffectCallback {
        void onSuccess(Bitmap result, String key);

        void onError(Exception e);

        void onStartProcess();

        void onFinished();
    }

    void applyEffect(@NonNull Bitmap bitmap, @NonNull ImageEffectCallback callback);

    void applyEffectWithData(@NonNull ImageEffectCallback callback, Context context);

    Boolean isBitmapHolder();
}
