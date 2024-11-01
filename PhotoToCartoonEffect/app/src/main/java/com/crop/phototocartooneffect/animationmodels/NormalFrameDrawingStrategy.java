package com.crop.phototocartooneffect.animationmodels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.crop.phototocartooneffect.activities.VideoFrames;
import com.crop.phototocartooneffect.activities.VideoRenderEngin;

public class NormalFrameDrawingStrategy implements FrameDrawingStrategy {

    @Override
    public void drawFrame(Bitmap bitmap, int screenWidth, int screenHeight, VideoFrames.FrameRate frameRate) {
        final int totalFrameCount = VideoRenderEngin.getFrameByTime(frameRate);
        Log.e("Rafiur>>total", "totalFrameCount: " + totalFrameCount);
        // Calculate the aspect ratio of the bitmap and the output video
        float bitmapAspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        float videoAspectRatio = (float) screenWidth / (float) screenHeight;

        int drawWidth, drawHeight;

        // Determine the size of the bitmap to draw
        if (bitmapAspectRatio > videoAspectRatio) {
            drawWidth = screenWidth;
            drawHeight = (int) (screenWidth / bitmapAspectRatio);
        } else {
            drawHeight = screenHeight;
            drawWidth = (int) (screenHeight * bitmapAspectRatio);
        }

        // Calculate the position to center the bitmap
        int left = (screenWidth - drawWidth) / 2;
        int top = (screenHeight - drawHeight) / 2;
        for (int i = 0; i < frameRate.getValue(); i++) {

            if (bitmap == null) {
                Log.e("Rafiur>>", "Bitmap is null, skipping drawFrame");
                bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(Color.GRAY);

            }

            try {

                for (int count = 0; count < totalFrameCount; count++) { // 10
                    VideoRenderEngin.getInstance().createCanvasForRender(Bitmap.createScaledBitmap(bitmap, drawWidth, drawHeight, true), left, top, new Paint());
                }
            } catch (Exception e) {
                Log.e("Rafiur>>", "Error in drawFrame: " + e.getMessage());
                e.printStackTrace();
            }

        }
    }
}
