package com.crop.phototocartooneffect.repositories;

import android.content.Context;

import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.AppSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppResources {

    private static AppResources instance;

    private AppResources() {
        // Private constructor to prevent instantiation
    }

    public static synchronized AppResources getInstance() {
        if (instance == null) {
            instance = new AppResources();
        }
        return instance;
    }

    public void reFreshAppItems(Context context, OnAppResourcesUpdatedListener listener) {
        // Example: Log individual fields
//        String userId = (String) imageData.get("userId");
//        String imageUrl = (String) imageData.get("imageUrl");
//        String prompt = (String) imageData.get("prompt");

        this.allItems.clear();
        FireStoreImageUploader.getInstance(context).getAllFeatureImages(new FireStoreImageUploader.GetAllImagesCallback() {
            @Override
            public void onSuccess(List<Map<String, Object>> imagesList) {
                for (Map<String, Object> image : imagesList) {

                    MenuItem menuItem = new MenuItem(image.get("title") != null ? image.get("title").toString() : "Default Title", // Default value for title
                            image.get("imageUrl") != null ? image.get("imageUrl").toString() : "",         // Default value for imageUrl
                            image.get("description") != null ? image.get("description").toString() : "No description available", // Default value for description
                            image.get("prompt") != null ? image.get("prompt").toString() : "",             // Default value for prompt
                            image.get("creationtype") != null ? ImageAiActivity.ImageCreationType.fromString(image.get("creationtype").toString())
                                    : ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION, // Assuming this is a constant
                            true // Assuming this is always true
                    );
                    final String menutype = image.get("menutype") != null ? image.get("menutype").toString() : "";
//                    FireStoreImageUploader.AITYPEFIREBASEDB aiType = FireStoreImageUploader.AITYPEFIREBASEDB.UNKNOWN;
//
//                    if (menutype.equalsIgnoreCase(FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI.toString())) {
//                        aiType = FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI;
//                    } else if (menutype.equalsIgnoreCase(FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2.toString())) {
//                        aiType = FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2;
//                    }
                    setItem(menuItem, FireStoreImageUploader.AITYPEFIREBASEDB.fromString(menutype));
                }
                listener.onFeaturedItemsUpdated();
            }

            @Override
            public void onError(String errorMessage) {
                listener.onFeaturedItemsUpdated();
            }
        });
    }


    public List<MenuItem> getItems(FireStoreImageUploader.AITYPEFIREBASEDB aiType) {
        if (!allItems.containsKey(aiType.getValue())) {
            allItems.put(aiType.getValue(), new ArrayList<>());
        }
        if (allItems.get(aiType.getValue()).size() == 0) {
            ArrayList<MenuItem> items = new ArrayList<>();
            if (aiType == FireStoreImageUploader.AITYPEFIREBASEDB.USERCREATIONS) {
                items.add(AppSettings.DEFAULT_ITEM);
                items.add(AppSettings.DEFAULT_ITEM);
                items.add(AppSettings.DEFAULT_ITEM);
                items.add(AppSettings.DEFAULT_ITEM);
            } else if (aiType == FireStoreImageUploader.AITYPEFIREBASEDB.USERCREATIONS_BANNER) {
                items.add(AppSettings.DEFAULT_ITEM);
            } else {
                items.add(AppSettings.DEFAULT_ITEM);
            }
            return items;
        } else {
            return allItems.get(aiType.getValue());
        }
    }

    public void setItem(MenuItem dataItem, FireStoreImageUploader.AITYPEFIREBASEDB aiType) {
        if (!allItems.containsKey(aiType.getValue())) {
            allItems.put(aiType.getValue(), new ArrayList<>());
        }
        allItems.get(aiType.getValue()).add(dataItem);

    }

    HashMap<String, ArrayList<MenuItem>> allItems = new HashMap<>();

    public interface OnAppResourcesUpdatedListener {
        void onFeaturedItemsUpdated();
    }


}
