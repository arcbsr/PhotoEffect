package com.crop.phototocartooneffect.firabsehelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signInAnonymously(AuthCallback callback) {
        if (mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(mAuth.getCurrentUser());
                } else {
                    callback.onFailure(task.getException());
                }
            });
        } else {
            callback.onSuccess(mAuth.getCurrentUser());
        }
    }

    public void checkAppVersion(AppVersionCallback callback) {
        db.collection("appsettings").document("settings").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                long version = documentSnapshot.getLong("version");
                callback.onVersionCheck(version);
            }
        }).addOnFailureListener(callback::onError);
    }

    public void initializeUserCoins(FirebaseUser user, long initialCoins) {
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && !task.getResult().exists()) {
                    db.collection("users").document(userId).set(new HashMap<String, Object>() {{
                        put("coins", initialCoins);
                    }});
                }
            });
        }
    }

    public void updateUserCoins(FirebaseUser user, int coinsToAdd) {
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).update("coins", FieldValue.increment(coinsToAdd));
        }
    }

    public void getUserCoinBalance(FirebaseUser user, CoinBalanceCallback callback) {
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    long coins = documentSnapshot.getLong("coins");
                    callback.onBalanceRetrieved(coins);
                }
            }).addOnFailureListener(callback::onError);
        }
    }

    public void saveCoinsLocally(Context context, long coins) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("coin_balance", coins);
        editor.apply();
    }

    public long getLocalCoinBalance(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("coin_balance", 0);
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(Exception e);
    }

    public interface AppVersionCallback {
        void onVersionCheck(long version);

        void onError(Exception e);
    }

    public interface CoinBalanceCallback {
        void onBalanceRetrieved(long balance);

        void onError(Exception e);
    }
}
