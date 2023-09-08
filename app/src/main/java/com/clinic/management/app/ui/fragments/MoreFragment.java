package com.clinic.management.app.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clinic.management.app.ClinicManagementApplication;
import com.clinic.management.app.databinding.FragmentMoreBinding;
import com.clinic.management.app.helper.StorageHelper;
import com.clinic.management.app.ui.activities.AppInfoActivity;
import com.clinic.management.app.ui.activities.ProfileActivity;
import com.clinic.management.app.ui.activities.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.containerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoreFragment.this.getContext(), ProfileActivity.class));
            }
        });

        binding.containerAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoreFragment.this.getContext(), SplashActivity.class));
            }
        });
        binding.containerAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoreFragment.this.getContext(), AppInfoActivity.class));
            }
        });
        binding.containerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                StorageHelper.clearCurrentUser();
                startActivity(new Intent(MoreFragment.this.getContext(), SplashActivity.class));
                MoreFragment.this.getActivity().finishAffinity();
            }
        });

        binding.containerLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClinicManagementApplication.getInstance());
                try {
                    String language = preferences.getString("language", Locale.getDefault().getLanguage());
                    if ("en".equalsIgnoreCase(language)) {
                        language = "ar";
                    } else {
                        language = "en";
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("language", language);
                    editor.apply();
                    startActivity(new Intent(getContext(), SplashActivity.class));
                    getActivity().finishAffinity();
                } catch (Exception ex) {
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}