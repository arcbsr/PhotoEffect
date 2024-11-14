package com.crop.phototocartooneffect.firabsehelper;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsHelper {

    private static AnalyticsHelper instance;
    private final FirebaseAnalytics firebaseAnalytics;

    // Private constructor to prevent instantiation from other classes
    private AnalyticsHelper(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    // Singleton instance method
    public static synchronized AnalyticsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AnalyticsHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Method to log a custom event with parameters
    public void logEvent(String eventName, Bundle eventParams) {
        firebaseAnalytics.logEvent(eventName, eventParams);
    }

    public void logEvent(String item_name, String item_category) {
        Bundle params = new Bundle();
        params.putString("item_name", item_name);
        params.putString("item_category", item_category);
        firebaseAnalytics.logEvent("item_viewed", params);
    }

    // Method to log a simple event with no parameters
    public void logEvent(String eventName) {
        firebaseAnalytics.logEvent(eventName, null);
    }

    // Method to set user properties
    public void setUserProperty(String propertyName, String propertyValue) {
        firebaseAnalytics.setUserProperty(propertyName, propertyValue);
    }

    // Method to set user ID for tracking
    public void setUserId(String userId) {
        firebaseAnalytics.setUserId(userId);
    }

    public class AnalyticsConstants {
        public static final String EVENT_SCREEN_VIEW = "screen_view";
        public static final String EVENT_ITEM_VIEWED = "item_viewed";
        public static final String PARAM_ITEM_NAME = "item_name";
        public static final String PARAM_ITEM_CATEGORY = "item_category";
    }
}

