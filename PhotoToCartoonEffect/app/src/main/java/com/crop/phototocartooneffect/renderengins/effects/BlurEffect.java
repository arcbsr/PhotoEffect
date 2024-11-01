//package com.crop.phototocartooneffect.renderengins.effects;
//
//import android.graphics.Bitmap;
//import android.renderscript.*;
//
//import androidx.annotation.NonNull;
//
//import com.crop.phototocartooneffect.renderengins.ImageEffect;
//
//public class BlurEffect implements ImageEffect {
//    private final RenderScript rs;
//    private final float radius;
//
//    public BlurEffect(RenderScript rs, float radius) {
//        this.rs = rs;
//        this.radius = radius;
//    }
//
//    @Override
//    public void applyEffect(@NonNull Bitmap bitmap, @NonNull ImageEffectCallback callback) {
//        try {
//            Bitmap blurredBitmap = applyBlur(bitmap);
//            callback.onSuccess(blurredBitmap);
//        } catch (Exception e) {
//            callback.onError(e);
//        }
//    }
//
//    @Override
//    public void applyEffectWithData(@NonNull Bitmap bitmap, @NonNull ImageEffectCallback callback) {
//
//    }
//
//    private Bitmap applyBlur(Bitmap original) {
//        Bitmap outputBitmap = Bitmap.createBitmap(original);
//        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        Allocation tmpIn = Allocation.createFromBitmap(rs, original);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//
//        blurScript.setRadius(radius);
//        blurScript.setInput(tmpIn);
//        blurScript.forEach(tmpOut);
//        tmpOut.copyTo(outputBitmap);
//
//        return outputBitmap;
//    }
//}
