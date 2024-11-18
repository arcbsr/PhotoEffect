package com.crop.phototocartooneffect.firabsehelper;

import android.content.Context;
import android.net.Uri;

import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreImageUploader {

    public enum AITYPEFIREBASEDB {
        FEATUREAI("featureai"), FEATUREAI2("featureai2"), USERCREATIONS("user_creations"), USERCREATIONS_BANNER("user_creations_banner"), UNKNOWN("unknown");

        private final String value;

        AITYPEFIREBASEDB(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final String STORAGE_NAME = "aiimages";
    private static final String TAG = "ImageUploader";
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    Context context;

    private static FireStoreImageUploader instance;

    private FireStoreImageUploader(Context context) {
        this.context = context.getApplicationContext();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FireStoreImageUploader getInstance(Context context) {
        if (instance == null) {
            instance = new FireStoreImageUploader(context);
        }
        return instance;
    }

    public void uploadImage(Uri imageUri, String userId, String prompt, AITYPEFIREBASEDB aiType) {
        if (!AppSettings.IS_ADMIN_MODE) {
            return;
        }
        if (imageUri == null) {
            RLog.e(TAG, "Image URI is null");
            return;
        }

        // Create a reference in Firebase Storage
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + userId + "/" + System.currentTimeMillis() + ".jpg");

        // Upload the image
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Handle upload success and failure
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Retrieve the download URL
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();

                // Save the URL in Firestore (optional)
                saveImageUrlToFirestorm(userId, downloadUrl, prompt, aiType);

                RLog.e(TAG, "Image uploaded successfully");
            });
        }).addOnFailureListener(e -> {
            RLog.e(TAG, "Image upload failed: " + e.getMessage());
        });
    }

    private void saveImageUrlToFirestorm(String userId, String downloadUrl, String prompt, AITYPEFIREBASEDB aiType) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("imageUrl", downloadUrl);
        data.put("prompt", prompt);
        data.put("menutype", aiType.getValue());

        db.collection(STORAGE_NAME).add(data).addOnSuccessListener(documentReference -> {
            RLog.e(TAG, "Image URL saved to Firestore");
        }).addOnFailureListener(e -> {
            RLog.e(TAG, "Failed to save URL: " + e.getMessage());
        });
    }

    public interface GetAllImagesCallback {
        void onSuccess(List<Map<String, Object>> imagesList);

        void onError(String errorMessage);
    }

    public void getAllFeatureImages(GetAllImagesCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(STORAGE_NAME).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                List<Map<String, Object>> imagesList = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Map<String, Object> imageData = document.getData();
                    imagesList.add(imageData);
                }
                RLog.e(TAG, "Images retrieved successfully");
                callback.onSuccess(imagesList);
            } else {
                callback.onError("No images found.");
            }
        }).addOnFailureListener(e -> {
            callback.onError("Error retrieving images: " + e.getMessage());
        });
    }

}
