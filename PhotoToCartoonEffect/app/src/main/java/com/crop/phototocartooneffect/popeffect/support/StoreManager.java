package com.crop.phototocartooneffect.popeffect.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StoreManager {
    public static int croppedLeft = 0;
    public static int croppedTop = 0;
    public static boolean isNull = false;
    private static String BITMAP_CROPED_FILE_NAME = "croped.tpc";
    private static String BITMAP_CROPED_MASK_FILE_NAME = "cropedmask.tpc";
    private static String BITMAP_FILE_NAME = "temp.tpc";
    private static String BITMAP_ORIGINAL_FILE_NAME = "original.tpc";
    private static String BITMAP_SPIRAL_FILE_NAME = "spiral.tpc";
    //TODO: temporary solution to fix android 10+ os sdcard path problem
    private static String BITMAP_TEMP_FILE_NAME = "";

    private static String generateFileName(Context context, String filename) {
        /*if (SupportedClass.stringIsNotEmpty(BITMAP_TEMP_FILE_NAME)) {
            return BITMAP_TEMP_FILE_NAME;
        }
        return SharePrefs.getStringValue(context, "temp_file") + filename;*/
        return filename;
    }

    public static void setCurrentEffecdedBitmap(Activity activity, Bitmap bitmap) {
    }

    public static String getRootPathToSaveFinalImage(Context context) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                + File.separator + context.getResources().getString(com.crop.phototocartooneffect.R.string.directory_final);
//        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                + context.getResources().getString(R.string.directory_final) + File.separator;
    }

    public static String getstorageRootPath(Context context) {
//        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator
//                + context.getResources().getString(R.string.directory_final);// + File.separator + context.getResources().getString(R.string.directory);
        return context.getFilesDir().getAbsolutePath();
    }

    public static Bitmap getCurrentCroppedMaskBitmap(Activity activity) {
        if (isNull) {
            return null;
        }
        return getBitmapByFileName(activity, generateFileName(activity, BITMAP_CROPED_MASK_FILE_NAME));
    }

    public static Bitmap getCurrentBitmap(Activity activity) {
        return getBitmapByFileName(activity, generateFileName(activity, BITMAP_FILE_NAME));
    }

    public static Bitmap getCurrentCropedBitmap(Activity activity) {
        if (isNull) {
            return null;
        }
        return getBitmapByFileName(activity, generateFileName(activity, BITMAP_CROPED_FILE_NAME));
    }

    public static Bitmap getCurrentOriginalBitmap(Activity activity) {
        //get Saved Bitmap
        return getBitmapByFileName(activity, generateFileName(activity, BITMAP_ORIGINAL_FILE_NAME));
    }

    private static Bitmap getBitmapByFileName(Activity r1, String r2) {
        String file_path = getstorageRootPath(r1) + File.separator + r2;
        Bitmap bitmap = BitmapFactory.decodeFile(file_path);
        return bitmap;
    }

    public static Bitmap getCurrentEffecdedBitmap(Activity activity) {
        return getCurrentBitmap(activity);
    }

    public static void saveFile(Context r2, Bitmap r3, String r4) {
        saveFileNew(r3, r4, r2);
    }

    /*
        Folder name must be thumb/
     */
    public static void saveFile(Context r2, Bitmap r3, String r4, String folder) {

        saveFileNew(r3, r4, r2, folder);
    }

    public static void saveSticker(Context r2, Bitmap r3, String r4, String folder) {

        saveFileNew(r3, r4, r2, folder);
    }

    public static void deleteFile(Context context, String str) {
        File file = new File(getstorageRootPath(context) + str);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void setCurrentBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, generateFileName(activity, BITMAP_FILE_NAME));
        }
        saveFile(activity, bitmap, generateFileName(activity, BITMAP_FILE_NAME));
    }

    public static void setCurrentCropedBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, generateFileName(activity, BITMAP_CROPED_FILE_NAME));
            isNull = true;
        } else {
            isNull = false;
        }
        saveFile(activity, bitmap, generateFileName(activity, BITMAP_CROPED_FILE_NAME));
    }

    public static void setCurrentCroppedMaskBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, generateFileName(activity, BITMAP_CROPED_MASK_FILE_NAME));
        }
        saveFile(activity, bitmap, generateFileName(activity, BITMAP_CROPED_MASK_FILE_NAME));
    }

    public static void setCurrentOriginalBitmap(Activity activity, Bitmap bitmap) {
        //save Saved Bitmap for to remove background in image
        if (bitmap == null) {
            deleteFile(activity, generateFileName(activity, BITMAP_ORIGINAL_FILE_NAME));
        }
        saveFile(activity, bitmap, generateFileName(activity, BITMAP_ORIGINAL_FILE_NAME));
    }

    public static Bitmap getCurrentSpiralBitmap(Activity activity) {
        return getBitmapByFileName(activity, generateFileName(activity, BITMAP_SPIRAL_FILE_NAME));
    }

    public static void setCurrentSpiralBitmap(Activity activity, Bitmap bitmap) {
        //save Saved Bitmap for to remove background in image
        if (bitmap == null) {
            deleteFile(activity, generateFileName(activity, BITMAP_SPIRAL_FILE_NAME));
        }
        saveFile(activity, bitmap, generateFileName(activity, BITMAP_SPIRAL_FILE_NAME));
    }

    private static void saveFileNew(Bitmap bitmap, String name, Context context) {
        saveFileNew(bitmap, name, context, "");
    }

    private static void saveFileNew(Bitmap bitmap, String name, Context context, String folder) {
        if (bitmap == null) {
            return;
        }
        if (name == null)
            return;
        try {
            String folder2 = "";
            if (folder != null && folder.length() > 0) {
                folder2 = File.separator + folder;
            }
            SaveFileUtils.saveBitmapFileTemp(context, bitmap, name, StoreManager.getstorageRootPath(context) + folder2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearAll(Activity context) {
        /*StoreManager.setCurrentCropedBitmap(context, null);
        StoreManager.setCurrentCroppedMaskBitmap(context, null);
        StoreManager.setCurrentSpiralBitmap(context, null);
        StoreManager.setCurrentEffecdedBitmap(context, null);
        StoreManager.setCurrentOriginalBitmap(context, null);*/
        try {
            File dir = new File(getstorageRootPath(context));
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getStickerList(Context context) {
        /*StoreManager.setCurrentCropedBitmap(context, null);
        StoreManager.setCurrentCroppedMaskBitmap(context, null);
        StoreManager.setCurrentSpiralBitmap(context, null);
        StoreManager.setCurrentEffecdedBitmap(context, null);
        StoreManager.setCurrentOriginalBitmap(context, null);*/
        ArrayList<String> names = new ArrayList<>();
        try {
            File dir = new File(getstorageRootPath(context), "mystickers");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    //new File(dir, children[i]).getName();
                    if (children[i].contains(".png")) {
                        names.add(children[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }
}
