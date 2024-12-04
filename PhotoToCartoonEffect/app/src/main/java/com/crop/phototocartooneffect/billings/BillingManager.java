package com.crop.phototocartooneffect.billings;

import android.content.Context;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.crop.phototocartooneffect.utils.PreferencesUtils;
import com.crop.phototocartooneffect.utils.RLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BillingManager {
    private static final String TAG = "BillingManager";

    private final Context context;
    private final BillingClient billingClient;
    private BillingManagerListener listener;

    public BillingManager(Context context) {
        this.context = context;

        // Initialize BillingClient
        billingClient = BillingClient.newBuilder(context).setListener((billingResult, purchases) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase purchase : purchases) {
                    if (listener != null) listener.onPurchaseSuccess(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                RLog.d(TAG, "User canceled the purchase.");
                if (listener != null) listener.onPurchaseCanceled();
            } else {
                RLog.e(TAG, "Error during purchase: " + billingResult.getDebugMessage());
                if (listener != null) listener.onPurchaseError(billingResult.getDebugMessage());
            }
        }).enablePendingPurchases().build();
    }

    // Connect to Google Play Billing
    public void startConnection(BillingManagerListener listener) {
        this.listener = listener;

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                RLog.d(TAG, "Billing service disconnected.");
            }

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    RLog.d(TAG, "Billing setup successful.");
                    if (listener != null) listener.onBillingSetupSuccess();
                } else {
                    RLog.e(TAG, "Billing setup failed: " + billingResult.getDebugMessage());
                }
            }
        });
    }

    // Query available subscription products
    public void queryAvailableSubscriptions(List<String> productIds) {
        List<QueryProductDetailsParams.Product> products = new ArrayList<>();
        for (String productId : productIds) {
            products.add(QueryProductDetailsParams.Product.newBuilder().setProductId(productId).setProductType(BillingClient.ProductType.SUBS).build());
        }

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder().setProductList(products).build();

        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                if (listener != null) listener.onSubscriptionDetailsFetched(productDetailsList);
            } else {
                RLog.e(TAG, "Failed to fetch subscription details: " + billingResult.getDebugMessage());
                if (listener != null)
                    listener.onErrorFetchingDetails(billingResult.getDebugMessage());
            }
        });
    }

    // Launch purchase flow for a subscription
    public void purchaseSubscription(ProductDetails productDetails) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(Collections.singletonList(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).build())).build();

        BillingResult billingResult = billingClient.launchBillingFlow((android.app.Activity) context, billingFlowParams);
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            RLog.e(TAG, "Purchase flow failed: " + billingResult.getDebugMessage());
        }
    }

    // Acknowledge a purchase
    public void acknowledgePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
            AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();

            billingClient.acknowledgePurchase(params, billingResult -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    RLog.d(TAG, "Purchase acknowledged successfully.");
                } else {
                    RLog.e(TAG, "Failed to acknowledge purchase: " + billingResult.getDebugMessage());
                }
            });
        }
    }

    public void checkIfAlreadyPurchased() {
        RLog.d(TAG, "Checking if user has an active subscription...");
        billingClient.queryPurchasesAsync(BillingClient.ProductType.SUBS, (billingResult, purchasesList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                boolean hasActiveSubscription = false;

                for (Purchase purchase : purchasesList) {
                    if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                        hasActiveSubscription = true;
                        break;
                    }
                }
                PreferencesUtils.getInstance(context).setSubscriptionStatus(hasActiveSubscription);
                RLog.d(TAG, "User has an active subscription: " + hasActiveSubscription);
                listener.onCheckCompleted(hasActiveSubscription);
            } else {
                RLog.e(TAG, "Failed to check purchases: " + billingResult.getDebugMessage());
                PreferencesUtils.getInstance(context).setSubscriptionStatus(false);
                listener.onCheckFailed(billingResult.getDebugMessage());
            }
        });
    }
}
