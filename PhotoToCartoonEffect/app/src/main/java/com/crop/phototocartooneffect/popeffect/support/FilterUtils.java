package com.crop.phototocartooneffect.popeffect.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class FilterUtils {

    public static Bitmap getBitmapWithFilter(Bitmap bitmap, String str) {
        return null;
    }


    public static Bitmap getBlurImageFromBitmap(Bitmap bitmap) {
        return null;
    }

    public static Bitmap getBlurImageFromBitmap(Bitmap bitmap, float f) {
        return null;
    }

    //TODO...
    public static Bitmap getBlurImageFromBitmaptest(Bitmap bitmap, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(Color.RED);
        canvas.drawRect(new RectF(0, createBitmap.getHeight() / 2, createBitmap.getWidth(), createBitmap.getHeight()), paint);
        //canvas.drawColor(Color.GREEN);
        return createBitmap;
    }

    static public Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry, boolean conformToOriginalPost) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        if (conformToOriginalPost) {
            path.rLineTo(0, ry);
            path.rLineTo(width, 0);
            path.rLineTo(0, -ry);
        } else {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    public static Bitmap cloneBitmap(Bitmap bitmap) {
        return bitmap;
    }

    public static Bitmap cloneBitmaps(Bitmap bitmap) {
        return null;
    }

    public static Bitmap getBlackAndWhiteImageFromBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Bitmap getMaskBodyFromBitmap(Bitmap bitmap, Bitmap Body, Context mContext, boolean isSplash) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = width * height;
        bitmap.getPixels(new int[i], 0, width, 0, 0, width, height);
        int[] iArr2 = new int[i];
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr2, 0, width, 0, 0, width, height);
        Bitmap cutBit = ImageUtils.getMask(mContext, bitmap, createBitmap, width, height);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                Body, cutBit.getWidth(), cutBit.getHeight(), true);
        if (!isSplash) {

            Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(resultBitmap);
            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(blur(mContext, bitmap), 0.0f, 0.0f, null);
            Bitmap marge = ImageUtils.getMarge(mContext, resizedBitmap, resultBitmap, width, height);
            return marge;
        } else {
            //cGEImageHandler.setFilterWithConfig("@adjust saturation 0");
            Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(resultBitmap);
            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            Bitmap marge = ImageUtils.getMarge(mContext, resizedBitmap, resultBitmap, width, height);
            return marge;
        }
    }

    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 50f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(25);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static Bitmap getEffectOnHair(Bitmap bitmap, Context context) {
        return null;
    }
}
