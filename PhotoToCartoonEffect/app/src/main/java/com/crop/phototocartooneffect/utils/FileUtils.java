package com.crop.phototocartooneffect.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtils {


    public static String getFilePathFromUri(Uri uri, Context context) {
        String filePath = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        if (columnIndex != -1) {
                            filePath = cursor.getString(columnIndex);
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        if (filePath == null) {
            filePath = uri.getPath();
        }
        return filePath;
    }
}
