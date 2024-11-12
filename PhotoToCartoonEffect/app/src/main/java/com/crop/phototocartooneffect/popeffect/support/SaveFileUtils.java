package com.crop.phototocartooneffect.popeffect.support;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveFileUtils {

    public static File saveBitmapFileEditor(Context context, Bitmap bitmap, String name) throws IOException {
        if (Build.VERSION.SDK_INT >= 30) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "VioEffect");
            OutputStream fos = resolver.openOutputStream(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "VioEffect";
            File file = new File(imagesDir);
            if (!file.exists()) {
                file.mkdir();
            }
            return new File(imagesDir, name + ".png");
        }
        String imagesDir2 = StoreManager.getRootPathToSaveFinalImage(context);
        File file2 = new File(imagesDir2);
        if (!file2.exists()) {
            file2.mkdir();
        }
        File image = new File(imagesDir2 + File.separator, name + ".png");
        Log.w("imagepath final", image.getAbsolutePath());
        OutputStream fos2 = new FileOutputStream(image);
        MediaScannerConnection.scanFile(context, new String[]{image.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String path, Uri uri) {
            }
        });
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos2);
        fos2.flush();
        fos2.close();
        return image;
    }

    public static File saveBitmapFileTemp(Context context, Bitmap bitmap, String name, String dir) throws IOException {
        //StoreManager.getRootPathToSaveFinalImage(context);
        //String imagesDir2 = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES;
//        String imagesDir2 = StoreManager.getstorageRootPath(context);
        File file2 = new File(dir);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File image = new File(dir, name);
        if (image.exists()) {
            image.delete();
        }
        OutputStream fos2 = new FileOutputStream(image);
       /* MediaScannerConnection.scanFile(context, new String[]{image.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String path, Uri uri) {
            }
        });*/
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos2);
        fos2.flush();
        fos2.close();
        return image;
    }

    public static File saveBitmapFileCollage(Context context, Bitmap bitmap, String name) throws IOException {
        String imagesDir2 = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES;
        File file2 = new File(imagesDir2);
        if (!file2.exists()) {
            file2.mkdir();
        }
        File image = new File(imagesDir2, name + ".png");
        OutputStream fos2 = new FileOutputStream(image);
        MediaScannerConnection.scanFile(context, new String[]{image.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String path, Uri uri) {
            }
        });
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos2);
        fos2.flush();
        fos2.close();
        return image;
    }

}
