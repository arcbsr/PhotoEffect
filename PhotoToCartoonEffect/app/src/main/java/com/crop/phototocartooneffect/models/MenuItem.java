package com.crop.phototocartooneffect.models;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;

import com.crop.phototocartooneffect.enums.EditingCategories;

public class MenuItem {
    public ValueAnimator animator;
    //    public String cloth = "https://pub-3626123a908346a7a8be8d9295f44e26.r2.dev/livewire-tmp/5BDmwvtizESFRO24uGDW1iu1u5TXhB-metaM2JmZmFkY2U5NDNkOGU3MDJhZDE0YTk2OTY2NjQ0NjYuanBn-.jpg";
    public EditingCategories.AITypeFirebaseClothTypeEDB clothType = EditingCategories.AITypeFirebaseClothTypeEDB.NONE;
    public EditingCategories.AILabExpressionType expressionType = EditingCategories.AILabExpressionType.NONE;
    private int iconResId;
    public String title;
    public Bitmap bitmap;
    private String description;

    public void setThumbResId(int thumbResId) {
        this.thumbResId = thumbResId;
    }

    private int thumbResId;
    public boolean isBanner;
    public boolean isPro = false;
    public String documentId;

    public void setImageCreationType(EditingCategories.ImageCreationType imageCreationType) {
        this.imageCreationType = imageCreationType;
    }

    private EditingCategories.ImageCreationType imageCreationType;

    public String prompt;
    public String imageUrl;

    public MenuItem(int iconResId, String title, String description, int thumbResId, EditingCategories.ImageCreationType imageCreationType, String prompt) {
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
        this.thumbResId = thumbResId;
        this.imageCreationType = imageCreationType;
        this.prompt = prompt;
    }

    public MenuItem(String title, String imageUrl, String description, String prompt, EditingCategories.ImageCreationType imageCreationType, boolean isPro) {
        this.isPro = isPro;
        this.title = title;
        this.description = description;
        this.imageCreationType = imageCreationType;
        this.prompt = prompt;
        this.imageUrl = imageUrl;
    }

    public MenuItem(int iconResId, String title, String description, int thumbResId, EditingCategories.ImageCreationType imageCreationType) {
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

    public EditingCategories.ImageCreationType getImageCreationType() {
        return imageCreationType;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "iconResId=" + iconResId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbResId=" + thumbResId +
                ", isPro=" + isPro +
                ", imageCreationType=" + imageCreationType.getValue() +
                ", prompt='" + prompt + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", documentId='" + documentId + '\'' +
                ", clothType=" + clothType.getValue() +
                ", expressionType=" + expressionType.getValue() +
                '}';
    }


}
