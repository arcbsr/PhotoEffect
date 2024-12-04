package com.crop.phototocartooneffect.repositories;

import android.content.Context;

import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader;
import com.crop.phototocartooneffect.enums.EditingCategories;
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
                            image.get("creationtype") != null ? EditingCategories.ImageCreationType.fromString(image.get("creationtype").toString()) : EditingCategories.ImageCreationType.FIREBASE_ML_SEGMENTATION, // Assuming this is a constant
                            true // Assuming this is always true
                    );
                    menuItem.clothType = image.get("clothtype") != null ? EditingCategories.AITypeFirebaseClothTypeEDB.fromString(image.get("clothtype").toString()) : EditingCategories.AITypeFirebaseClothTypeEDB.NONE;
                    menuItem.expressionType = image.get("expressiontype") != null ?
                            EditingCategories.AILabExpressionType.fromString2(image.get("expressiontype").toString()) :
                            EditingCategories.AILabExpressionType.NONE;
                    menuItem.isPro = image.get("isPro") != null ? (boolean) image.get("isPro") : false;
                    menuItem.documentId = image.get("documentId") != null ? image.get("documentId").toString() : "";
                    final String menutype = image.get("menutype") != null ? image.get("menutype").toString() : "";
//                    FireStoreImageUploader.AITYPEFIREBASEDB aiType = FireStoreImageUploader.AITYPEFIREBASEDB.UNKNOWN;
//
//                    if (menutype.equalsIgnoreCase(FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI.toString())) {
//                        aiType = FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI;
//                    } else if (menutype.equalsIgnoreCase(FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2.toString())) {
//                        aiType = FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2;
//                    }
                    setItem(menuItem, EditingCategories.AITypeFirebaseEDB.fromString(menutype));
                }
                listener.onFeaturedItemsUpdated();
            }

            @Override
            public void onError(String errorMessage) {
                listener.onFeaturedItemsUpdated();
            }
        });
    }

    public void getAllPromptsItems(Context context, OnAppResourcesUpdatedListener listener) {
        this.allItems.clear();
        FireStoreImageUploader.getInstance(context).getAllPrompts(new FireStoreImageUploader.GetAllImagesCallback() {
            @Override
            public void onSuccess(List<Map<String, Object>> imagesList) {
                allPrompts.clear();
                allPrompts.add(0, "");
                for (Map<String, Object> image : imagesList) {
                    final String prompt = image.get("prompt") != null ? image.get("prompt").toString() : "";
                    allPrompts.add(prompt);
                }
                listener.onFeaturedItemsUpdated();
            }

            @Override
            public void onError(String errorMessage) {
                listener.onFeaturedItemsUpdated();
            }
        });
    }

    public List<MenuItem> getItems(EditingCategories.AITypeFirebaseEDB aiType) {
        if (!allItems.containsKey(aiType.getValue())) {
            allItems.put(aiType.getValue(), new ArrayList<>());
        }
        if (allItems.get(aiType.getValue()).size() == 0) {
            ArrayList<MenuItem> items = new ArrayList<>();
            if (aiType == EditingCategories.AITypeFirebaseEDB.USERCREATIONS) {
                items.add(AppSettings.getRandomItem());
                items.add(AppSettings.getRandomItem());
                items.add(AppSettings.getRandomItem());
                items.add(AppSettings.getRandomItem());
            } else if (aiType == EditingCategories.AITypeFirebaseEDB.USERCREATIONS_BANNER) {
//                items.add(AppSettings.DEFAULT_ITEM);
            } else {
                items.add(AppSettings.getRandomItem());
            }
            return items;
        }

        if (aiType == EditingCategories.AITypeFirebaseEDB.USERCREATIONS) {
            int remainder = allItems.get(aiType.getValue()).size() % 4;
            if (remainder != 0) {
                for (int i = 0; i < 4 - remainder; i++) {
                    allItems.get(aiType.getValue()).add(AppSettings.getRandomItem());
                }
            }
            int divider = allItems.get(aiType.getValue()).size() / 4;
            if (!allItems.containsKey(EditingCategories.AITypeFirebaseEDB.USERCREATIONS_BANNER.getValue())) {
                allItems.put(EditingCategories.AITypeFirebaseEDB.USERCREATIONS_BANNER.getValue(), new ArrayList<>());
            }
            int diffForBanner = divider - (!allItems.containsKey(EditingCategories.AITypeFirebaseEDB.USERCREATIONS_BANNER.getValue()) ? 0 :
                    allItems.get(EditingCategories.AITypeFirebaseEDB.USERCREATIONS_BANNER.getValue()).size());
            if (diffForBanner > 0) {
                for (int i = 0; i < diffForBanner; i++) {
                    allItems.get(EditingCategories.AITypeFirebaseEDB.USERCREATIONS_BANNER.getValue()).add(AppSettings.DEFAULT_ITEM);
                }
            }
        }
        return allItems.get(aiType.getValue());

    }

    public void setItem(MenuItem dataItem, EditingCategories.AITypeFirebaseEDB aiType) {
        if (!allItems.containsKey(aiType.getValue())) {
            allItems.put(aiType.getValue(), new ArrayList<>());
        }
        allItems.get(aiType.getValue()).add(dataItem);

    }

    HashMap<String, ArrayList<MenuItem>> allItems = new HashMap<>();

    public ArrayList<String> getAllPrompts() {
        return allPrompts;
    }

    ArrayList<String> allPrompts = new ArrayList<>();

    public interface OnAppResourcesUpdatedListener {
        void onFeaturedItemsUpdated();
    }


}
