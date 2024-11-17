package com.crop.phototocartooneffect.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.firabsehelper.FirebaseHelper;
import com.crop.phototocartooneffect.models.Subscription;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionFragment extends BaseBottomFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        init(view);
        return view;
    }


    private void init(View view) {
        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
            dismiss();
        });

        createSubscription(view);
    }

    private void createSubscription(View view) {

        View view1 = view.findViewById(R.id.subs_1);
        View view2 = view.findViewById(R.id.subs_2);
        View view3 = view.findViewById(R.id.subs_3);
        FirebaseHelper.getInstance().getSubscriptions(new FirebaseHelper.FetchSubscriptionsCallback() {
            @Override
            public void onSuccess(List<Subscription> subscriptions) {
                if (subscriptions.size() == 3) {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);
                } else if (subscriptions.size() == 2) {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.GONE);
                } else if (subscriptions.size() == 1) {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                } else {
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                }
                if (subscriptions.size() > 0) {
                    addItems(view1, subscriptions.get(0));
                }
                if (subscriptions.size() > 1) {
                    addItems(view2, subscriptions.get(1));
                }
                if (subscriptions.size() > 2) {
                    addItems(view3, subscriptions.get(2));
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    private void addItems(View inflateView, Subscription subscription) {
        ((TextView) inflateView.findViewById(R.id.subscriptionTitle)).setText(subscription.getTitle());
        ((TextView) inflateView.findViewById(R.id.subscriptionPrice)).setText(subscription.getPrice() + "/month");
        ((TextView) inflateView.findViewById(R.id.subscriptionDescription)).setText(subscription.getDescription());
    }

}