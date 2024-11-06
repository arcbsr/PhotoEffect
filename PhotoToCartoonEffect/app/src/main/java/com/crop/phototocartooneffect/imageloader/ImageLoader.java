package com.crop.phototocartooneffect.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.crop.phototocartooneffect.activities.BitmapCache;
import com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2;
import com.crop.phototocartooneffect.utils.RLog;

import java.io.ByteArrayOutputStream;

public class ImageLoader {

    private static ImageLoader instance;
    private BitmapCache bitmapCache;


    private ImageLoader() {
        bitmapCache = new BitmapCache();
    }

    public static synchronized ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public Bitmap getBitmap(String key, OnImageLoadedListener listener) {
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key);

        if (cachedBitmap != null) {
            listener.onImageLoaded(cachedBitmap, key, -1);
            return cachedBitmap;
        }
        return null;

    }

    public Bitmap getBitmap(String key) {
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key);

        if (cachedBitmap != null) {
            return cachedBitmap;
        }
        return null;

    }

    public String getBitmapAsBase64(Bitmap bitmap) {
        String base64 = "";

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            base64 = "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
            RLog.e("getBitmapAsBase64", "Base64 encoding successful>>" + base64);
        } else {
        }

        return base64;
    }

    public String getBitmapAsBase64(String key) {
        // Log key and ensure the cache is checked properly
        RLog.e("getBitmapAsBase64", "Looking for bitmap with key: " + key);

        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key);
        String base64 = "";

        if (cachedBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            cachedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            base64 = "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
            RLog.e("getBitmapAsBase64", "Base64 encoding successful.");
        } else {
            RLog.e("getBitmapAsBase64", "Bitmap not found in cache for key: " + key);
        }

        return base64;
    }


    public interface OnImageLoadedListener {
        void onImageLoaded(Bitmap bitmap, String keyValue, int position);
    }

    public void loadBitmap(String key, Bitmap bitmap) {
        bitmapCache.addBitmapToCache(key, bitmap);
    }

    public void loadBitmap(Context context, String url, String key2, com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2 listener) {
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key2);

        if (cachedBitmap != null) {
            listener.onImageLoaded(cachedBitmap, key2, 0);
        } else {
            try {
                Glide.with(context).asBitmap().load(url).into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        bitmapCache.addBitmapToCache(key2, resource);
                        listener.onImageLoaded(resource, key2, 0);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (listener instanceof OnImageLoadedListener2) {
                            listener.onErrorLoaded(url, 0);
                        }
                    }
                });
            } catch (Exception e) {
                if (listener instanceof OnImageLoadedListener2) {
                    listener.onErrorLoaded(url, 0);
                }
            }
        }
    }

    public void loadBitmap(Context context, String url, String key2, OnImageLoadedListener listener) {
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key2);

        if (cachedBitmap != null) {
            listener.onImageLoaded(cachedBitmap, key2, 0);
        } else {
            Glide.with(context).asBitmap().load(url).into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                    bitmapCache.addBitmapToCache(key2, resource);
                    listener.onImageLoaded(resource, key2, 0);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    if (listener instanceof OnImageLoadedListener2) {
                        ((OnImageLoadedListener2) listener).onErrorLoaded(url, 0);
                    }
                }
            });
        }
    }

    public void loadBitmap(Context context, Uri uri, int position, OnImageLoadedListener listener) {
        String key2 = System.currentTimeMillis() + "_" + position;
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key2);

        if (cachedBitmap != null) {
            listener.onImageLoaded(cachedBitmap, key2, position);
        } else {
            Glide.with(context).asBitmap().load(uri).into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                    bitmapCache.addBitmapToCache(key2, resource);
                    listener.onImageLoaded(resource, key2, position);
                }
            });
        }

    }
}
