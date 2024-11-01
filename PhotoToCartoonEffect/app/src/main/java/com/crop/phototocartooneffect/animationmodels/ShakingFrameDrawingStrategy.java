package com.crop.phototocartooneffect.animationmodels;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.crop.phototocartooneffect.activities.VideoFrames;
import com.crop.phototocartooneffect.activities.VideoRenderEngin;

public class ShakingFrameDrawingStrategy implements FrameDrawingStrategy {

    @Override
    public void drawFrame(Bitmap bitmap, int screenWidth, int screenHeight, VideoFrames.FrameRate frameRate) {
        final int totalFrameCount = VideoRenderEngin.getFrameByTime(frameRate) * frameRate.getValue();
        Log.e("Rafiur>>total", "totalFrameCount: " + totalFrameCount);
        int shakeAmplitude = 25; // Maximum shake offset in pixels
        int counter = 0;

        // Calculate the aspect ratio of the bitmap and the output video
        float bitmapAspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        float videoAspectRatio = (float) screenWidth / (float) screenHeight;
        int drawWidth, drawHeight;

        // Calculate draw dimensions to maintain aspect ratio
        if (bitmapAspectRatio > videoAspectRatio) {
            drawWidth = screenWidth;
            drawHeight = (int) (screenWidth / bitmapAspectRatio);
        } else {
            drawHeight = screenHeight;
            drawWidth = (int) (screenHeight * bitmapAspectRatio);
        }

        // Calculate the center position of the bitmap
        int baseLeft = (screenWidth - drawWidth) / 2;
        int baseTop = (screenHeight - drawHeight) / 2;

        long previousTime = System.currentTimeMillis();

        do {
            // Apply a small random offset for shake effect
            int shakeX = (int) (Math.random() * shakeAmplitude * 2) - shakeAmplitude;
            int shakeY = (int) (Math.random() * shakeAmplitude * 2) - shakeAmplitude;

            int left = baseLeft + shakeX;
            int top = baseTop + shakeY;

            // Create a matrix for translation
            Matrix matrix = new Matrix();
            matrix.postTranslate(left, top);

            // Draw the bitmap on the canvas with the applied shake effect
            VideoRenderEngin.getInstance().createCanvasForRender(bitmap, matrix, new Paint(), VideoRenderEngin.defaultFrameTime);

            // Calculate elapsed time to control shake duration
            long currentTime = System.currentTimeMillis();
            float elapsedTimeSec = (currentTime - previousTime) / 1000.0f;
            previousTime = currentTime;
            counter++;
        } while (counter < totalFrameCount); // Run for the number of frames specified by the frame rate
    }
}
