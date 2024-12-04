package com.crop.phototocartooneffect.dialogfragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.billings.BillingManager;
import com.crop.phototocartooneffect.billings.BillingManagerListener;
import com.crop.phototocartooneffect.firabsehelper.FirebaseHelper;
import com.crop.phototocartooneffect.models.Subscription;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionFragment extends BaseBottomFragment implements BillingManagerListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        init(rootView);
        return rootView;
    }

    View rootView;
    BillingManager billingManager;

    private void init(View view) {
        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
            dismiss();
        });
        billingManager = new BillingManager(getContext());
        billingManager.startConnection(this);

    }

    private void createSubscription(View view) {
        FirebaseHelper.getInstance().getSubscriptions(new FirebaseHelper.FetchSubscriptionsCallback() {
            @Override
            public void onSuccess(List<Subscription> subscriptions) {
                ((LinearLayout) view.findViewById(R.id.root_view_sublist)).removeAllViews();

                if (subscriptions.size() == 0) {
                    Toast.makeText(getContext(), "No subscriptions available", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }

//
//                // Example: Fetch subscriptions
//                List<String> productIds = Arrays.asList("premium_subscription", "pro_subscription");
//                billingManager.queryAvailableSubscriptions(productIds);
                expansionLayoutCollection = new ExpansionLayoutCollection();
                for (Subscription subscription : subscriptions) {
                    addItems(view, subscription);
                }
                view.findViewById(R.id.root_view_sublist).post(() -> {
                    expansionLayoutCollection.openOnlyOne(true);
                    ((ExpansionLayout) ((LinearLayout) view.findViewById(R.id.root_view_sublist)).getChildAt(0)
                            .findViewById(R.id.expansionLayout)).expand(true);

                    TextView tvLinks = view.findViewById(R.id.tvLinks);
                    String privacyText = getString(R.string.privacy);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvLinks.setText(Html.fromHtml(privacyText, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        tvLinks.setText(Html.fromHtml(privacyText));
                    }
                    tvLinks.setMovementMethod(LinkMovementMethod.getInstance());
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    ExpansionLayoutCollection expansionLayoutCollection;

    private void addItems(View view, Subscription subscription) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View inflateView = inflater.inflate(R.layout.subs_item, null);
        ((TextView) inflateView.findViewById(R.id.title_text)).setText(subscription.getTitle() + " (" + subscription.getPrice() + "/month)\n"
                + subscription.getDescription());
        ((TextView) inflateView.findViewById(R.id.subscriptionTitle)).setText(subscription.getTitle());
        ((TextView) inflateView.findViewById(R.id.subscriptionPrice)).setText(subscription.getPrice() + "/month");
        ((TextView) inflateView.findViewById(R.id.subscriptionDescription)).setText(Html.fromHtml(subscription.getFacilities(), Html.FROM_HTML_MODE_LEGACY));
        // Add to parent
        ((LinearLayout) view.findViewById(R.id.root_view_sublist)).addView(inflateView);
        expansionLayoutCollection.add(inflateView.findViewById(R.id.expansionLayout));
    }

    @Override
    public void onBillingSetupSuccess() {
        billingManager.checkIfAlreadyPurchased();
        createSubscription(rootView);
    }

    @Override
    public void onSubscriptionDetailsFetched(List<ProductDetails> productDetailsList) {

    }

    @Override
    public void onPurchaseSuccess(Purchase purchase) {

    }

    @Override
    public void onPurchaseCanceled() {

    }

    @Override
    public void onPurchaseError(String error) {

    }

    @Override
    public void onErrorFetchingDetails(String error) {

    }

    @Override
    public void onCheckCompleted(boolean hasActiveSubscription) {
        if (hasActiveSubscription) {
            dismiss();
        }
    }

    @Override
    public void onCheckFailed(String error) {

    }
}