package com.crop.phototocartooneffect.billings;

import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;

import java.util.List;

public interface BillingManagerListener {
    void onBillingSetupSuccess();

    void onSubscriptionDetailsFetched(List<ProductDetails> productDetailsList);

    void onPurchaseSuccess(Purchase purchase);

    void onPurchaseCanceled();

    void onPurchaseError(String error);

    void onErrorFetchingDetails(String error);

    void onCheckCompleted(boolean hasActiveSubscription);

    void onCheckFailed(String error);
}
