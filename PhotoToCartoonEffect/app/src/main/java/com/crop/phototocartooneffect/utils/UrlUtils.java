package com.crop.phototocartooneffect.utils;

import java.net.URL;

public class UrlUtils {
    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
