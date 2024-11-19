package com.crop.phototocartooneffect.firabsehelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreImageUploader {

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

//    public void uploadImage(Bitmap bitmap, String userId, String prompt, AITYPEFIREBASEDB aiType) {
//        if (!AppSettings.IS_ADMIN_MODE) {
//            return;
//        }
//        if (imageUri == null) {
//            RLog.e(TAG, "Image URI is null");
//            return;
//        }
//
//        // Create a reference in Firebase Storage
//        StorageReference storageRef = storage.getReference();
//        StorageReference imageRef = storageRef.child("images/" + userId + "/" + System.currentTimeMillis() + ".jpg");
//
//        // Upload the image
//        UploadTask uploadTask = imageRef.putFile(imageUri);
//
//        // Handle upload success and failure
//        uploadTask.addOnSuccessListener(taskSnapshot -> {
//            // Retrieve the download URL
//            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                String downloadUrl = uri.toString();
//
//                // Save the URL in Firestore (optional)
//                saveImageUrlToFirestorm(userId, downloadUrl, prompt, aiType);
//
//                RLog.e(TAG, "Image uploaded successfully");
//            });
//        }).addOnFailureListener(e -> {
//            RLog.e(TAG, "Image upload failed: " + e.getMessage());
//        });
//    }


    public void uploadImageToDB(Uri imageUri, String userId, String prompt, EditingCategories.AITypeFirebaseEDB aiType, EditingCategories.ImageCreationType imageCreationType,
                                EditingCategories.AITypeFirebaseClothTypeEDB aiTypeFirebaseClothTypeEDB,
                                ImageDownloadCallback downloadCallback) {

        uploadImage(imageUri, aiType.getValue(), new ImageDownloadCallback() {
            @Override
            public void onSuccess(String url) {
                saveImageUrlToFirestorm(userId, url, prompt, aiType, imageCreationType, downloadCallback, aiTypeFirebaseClothTypeEDB.getValue());
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        RLog.e(TAG, "Image uploaded successfully");

    }

    public void uploadImageToDB(Bitmap bitmap, String userId, String prompt, EditingCategories.AITypeFirebaseEDB aiType,
                                EditingCategories.ImageCreationType imageCreationType, EditingCategories.AITypeFirebaseClothTypeEDB aiTypeFirebaseClothTypeEDB,
                                ImageDownloadCallback downloadCallback) {

        uploadImage(bitmap, aiType.getValue(), new ImageDownloadCallback() {
            @Override
            public void onSuccess(String url) {
                saveImageUrlToFirestorm(userId, url, prompt, aiType, imageCreationType, downloadCallback, aiTypeFirebaseClothTypeEDB.getValue());
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        RLog.e(TAG, "Image uploaded successfully");

    }

    public void uploadImage(Uri imageUri, String fileName, ImageDownloadCallback imageDownloadCallback) {
        if (!AppSettings.IS_ADMIN_MODE) {
            return;
        }
        if (imageUri == null) {
            RLog.e(TAG, "Image URI is null");
            return;
        }

        // Create a reference in Firebase Storage
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + fileName + "/" + System.currentTimeMillis() + ".jpg");

        // Upload the image
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Handle upload success and failure
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Retrieve the download URL
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();

                // Save the URL in Firestore (optional)

                RLog.e(TAG, "Image uploaded successfully");
                imageDownloadCallback.onSuccess(downloadUrl);
            });
        }).addOnFailureListener(e -> {
            RLog.e(TAG, "Image upload failed: " + e.getMessage());
            imageDownloadCallback.onFailure(e.getMessage());
        });
    }

    public void uploadImage(Bitmap bitmap, String fileName, ImageDownloadCallback imageDownloadCallback) {
        // Reference to Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file (you can choose any path or file name)
        StorageReference imageRef = storageRef.child("images/" + fileName + "/" + System.currentTimeMillis() + ".jpg");

        // Convert Bitmap to ByteArray
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Compress the bitmap
        byte[] data = baos.toByteArray();

        // Upload ByteArray to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Get and use the download URL
                String downloadUrl = uri.toString();
                RLog.d("Firebase", "Image uploaded successfully. URL: " + downloadUrl);
                imageDownloadCallback.onSuccess(downloadUrl);
            });
        }).addOnFailureListener(e -> {
            // Handle failure
            RLog.e("Firebase", "Image upload failed: " + e.getMessage());
            imageDownloadCallback.onFailure(e.getMessage());
        });
    }

    private void saveImageUrlToFirestorm(String userId, String downloadUrl, String prompt, EditingCategories.AITypeFirebaseEDB aiType, EditingCategories.ImageCreationType imageCreationType, ImageDownloadCallback downloadCallback) {
        saveImageUrlToFirestorm(userId, downloadUrl, prompt, aiType, imageCreationType, downloadCallback, "");
    }

    private void saveImageUrlToFirestorm(String userId, String downloadUrl, String prompt, EditingCategories.AITypeFirebaseEDB aiType, EditingCategories.ImageCreationType imageCreationType, ImageDownloadCallback downloadCallback,
                                         String clothType) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("imageUrl", downloadUrl);
        data.put("prompt", prompt);
        data.put("clothtype", clothType);
        data.put("menutype", aiType.getValue());
        data.put("creationtype", imageCreationType.getValue());

        db.collection(STORAGE_NAME).add(data).addOnSuccessListener(documentReference -> {
            RLog.e(TAG, "Image URL saved to Firestore");
            downloadCallback.onSuccess(downloadUrl);
        }).addOnFailureListener(e -> {
            RLog.e(TAG, "Failed to save URL: " + e.getMessage());
            downloadCallback.onFailure(e.getMessage());
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

    public void deleteImage(String imageUrl) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(imageUrl);
        storageRef.delete().addOnSuccessListener(unused -> {
            RLog.e(TAG, "Image deleted successfully");
        });
    }

    public interface ImageDownloadCallback {
        void onSuccess(String url);

        void onFailure(String errorMessage);
    }
}
