package com.crop.phototocartooneffect.animationmodels;

import android.graphics.Bitmap;

import com.crop.phototocartooneffect.activities.VideoFrames;

public interface FrameDrawingStrategy {
    void drawFrame(Bitmap bitmap, int screenWidth, int screenHeight, VideoFrames.FrameRate frameRate);
}
