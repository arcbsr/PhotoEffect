package com.crop.phototocartooneffect.dialogfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.firabsehelper.FirebaseHelper;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.Constants;
import com.crop.phototocartooneffect.utils.PreferencesUtils;
import com.crop.phototocartooneffect.utils.RLog;
import com.google.firebase.auth.FirebaseUser;

public class ContactUsFragment extends BaseBottomFragment {

    public boolean hasContactedToday(Context context) {
        if (AppSettings.IS_TESTING_MODE_MINOR) {
            return false;
        }
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long lastContactTime = getPreferencesUtils().getLong(Constants.CONTACT_US_TIME, 0);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastContactTime) < oneDayInMillis;

    }

    public void recordContactAttempt(Context context) {
        getPreferencesUtils().setLong(Constants.CONTACT_US_TIME, System.currentTimeMillis());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactus_dialog, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.submitButton).setOnClickListener(v -> {
            submitForm(view);
        });
        ((EditText) view.findViewById(R.id.emailEditText)).setText(getPreferencesUtils().getString("email", ""));
    }

    private void submitForm(View view) {
        final String email = ((EditText) view.findViewById(R.id.emailEditText)).getText().toString();
        final String msg = ((EditText) view.findViewById(R.id.descriptionEditText)).getText().toString();
        final String subject = ((EditText) view.findViewById(R.id.subjectEditText)).getText().toString();
        final String appVersion = AppSettings.getAppVersion(getContext());
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ((EditText) view.findViewById(R.id.emailEditText)).setError(getString(R.string.invalid_email));
            return;
        }
        if (msg.isEmpty()) {
            ((EditText) view.findViewById(R.id.descriptionEditText)).setError(getString(R.string.msg_required));
            return;
        }
        PreferencesUtils.getInstance(getContext()).setString("email", email);
        FirebaseHelper.getInstance().signInAnonymously(new FirebaseHelper.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                RLog.e("ContactUs", "onSuccess: " + user.getUid());
                FirebaseHelper.getInstance().submitFeedback(user, msg, appVersion, email, subject);
                recordContactAttempt(getContext());
                dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                RLog.e("ContactUs", "onFailure: " + e.getMessage());
                dismiss();
            }
        });
    }
}