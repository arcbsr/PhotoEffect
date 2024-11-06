package com.crop.phototocartooneffect.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2;
import com.crop.phototocartooneffect.utils.RLog;

import androidx.annotation.Nullable;

public class ImageLoaderWithRetries {
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_INTERVAL_MS = 5000;

    private final Context context;
    private final Handler handler;
    private int retryCount = 0;

    public ImageLoaderWithRetries(Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void loadImage(final String imageUrl, OnImageLoadedListener2 callBack) {
        loadImageWithRetry(imageUrl, callBack);
    }

    private void loadImageWithRetry(final String imageUrl, final OnImageLoadedListener2 callBack) {
        RLog.e("ImageLoaderWithRetries", "Loading image with retry: " + imageUrl);
        Glide.with(context).asBitmap().load(imageUrl).into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                RLog.e("ImageLoaderWithRetries", "Image loaded successfully: " + imageUrl);
                String keyv = System.currentTimeMillis() + "";
                ImageLoader.getInstance().loadBitmap(keyv, resource);
                callBack.onImageLoaded(resource, keyv, 0);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                RLog.e("ImageLoaderWithRetries", "Image load failed: " + retryCount);
                if (retryCount < MAX_RETRIES) {
                    retryCount++;
                    handler.postDelayed(() -> loadImageWithRetry(imageUrl, callBack), RETRY_INTERVAL_MS);
                } else {
                    callBack.onErrorLoaded(imageUrl, -1);
                }
            }
        });
//        Glide.with(context)
//                .asBitmap()
//                .load(imageUrl)
//                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        RLog.e("ImageLoaderWithRetries", "Image load failed: " + retryCount);
//                        if (retryCount < MAX_RETRIES) {
//                            retryCount++;
//                            handler.postDelayed(() -> loadImageWithRetry(imageUrl, callBack), RETRY_INTERVAL_MS);
//                        } else {
//                            callBack.onErrorLoaded(imageUrl, -1);
//                        }
//                        return true; // Indicates we handled the error
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        callBack.onImageLoaded(resource, imageUrl, 0);
//                        return false; // Indicates Glide should handle the bitmap
//                    }
//                });
    }
}