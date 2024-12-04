package com.crop.phototocartooneffect.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

    private static final String PREF_NAME = "PhotoToCartoonEffectPrefs";
    private SharedPreferences sharedPreferences;


    private static PreferencesUtils instance;

    private PreferencesUtils() {
        // Private constructor to prevent instantiation
    }

    public static PreferencesUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtils();
        }
        instance.init(context);
        return instance;
    }

    private void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE);
        }
    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void setLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void setFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void setSubscriptionStatus(boolean hasActiveSubscription) {
        setBoolean("subscription_status", hasActiveSubscription);
    }

    public boolean getSubscriptionStatus() {
        return getBoolean("subscription_status", false);
    }

}
