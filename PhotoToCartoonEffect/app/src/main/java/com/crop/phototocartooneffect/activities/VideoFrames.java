package com.crop.phototocartooneffect.activities;

import com.crop.phototocartooneffect.animationmodels.FrameDrawingStrategy;
import com.crop.phototocartooneffect.animationmodels.NormalFrameDrawingStrategy;

public class VideoFrames {
    public VideoFrames(String imageKey, int position, FrameDrawingStrategy animationStrategy) {
        this.imageKey = imageKey;
        this.position = position;
        this.animationStrategy = animationStrategy;
    }

    public VideoFrames(String imageKey, int position) {
        this.imageKey = imageKey;
        this.position = position;
        this.animationStrategy = new NormalFrameDrawingStrategy();
    }

    public FrameDrawingStrategy getAnimationStrategy() {
        return animationStrategy;
    }

    private FrameDrawingStrategy animationStrategy;
    int position = 0;
    private String imageKey = "";
    private FrameRate frameRate = FrameRate.FIVE_SECOND;

    public FrameRate getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(FrameRate frameRate) {
        this.frameRate = frameRate;
    }

    public String getFrameKey() {
        return imageKey;
    }

    public enum FrameRate {
        DEFAULT(1),
        ONE_SECOND(1),
        TWO_SECOND(2),
        THREE_SECOND(3),
        FOUR_SECOND(4),
        FIVE_SECOND(5),
        Lower(1),
        NO_FRAME(0);

        private final int value;

        FrameRate(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
