package com.crop.phototocartooneffect.models;

import com.crop.phototocartooneffect.activities.ImageAiActivity;

public class MenuItem {
    private int iconResId;
    private String title;
    private String description;
    private int thumbResId;
    public boolean isBanner;
    private ImageAiActivity.ImageCreationType imageCreationType;

    public String prompt;

    public MenuItem(int iconResId, String title, String description, int thumbResId, ImageAiActivity.ImageCreationType imageCreationType, String prompt) {
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
        this.thumbResId = thumbResId;
        this.imageCreationType = imageCreationType;
        this.prompt = prompt;
    }

    public MenuItem(int iconResId, String title, String description, int thumbResId, ImageAiActivity.ImageCreationType imageCreationType) {
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
        this.thumbResId = thumbResId;
        this.imageCreationType = imageCreationType;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getThumbResId() {
        return thumbResId;
    }

    public ImageAiActivity.ImageCreationType getImageCreationType() {
        return imageCreationType;
    }
}
