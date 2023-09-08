package com.clinic.management.app.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clinic.management.app.ClinicManagementApplication;
import com.clinic.management.app.databinding.FragmentAboutBinding;
import com.clinic.management.app.models.About;
import com.clinic.management.app.models.Constants;
import com.clinic.management.app.utils.ToastUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AboutFragment extends Fragment {
    FragmentAboutBinding binding;

    private DatabaseReference reference;
    private ValueEventListener valueEventListenerUser;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        reference = FirebaseDatabase.getInstance().getReference(Constants.NODE_NAME_ABOUT + "/" + PreferenceManager.getDefaultSharedPreferences(ClinicManagementApplication.getInstance()).getString("language", Locale.getDefault().getLanguage()));
        valueEventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    About about = snapshot.getValue(About.class);
                    binding.objectives.setText(about.getObjectives());
                    binding.conditions.setText(about.getConditions());
                    binding.content.setText(about.getContent());
                } catch (Exception ex) {
                    ToastUtils.longToast(ex.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addValueEventListener(valueEventListenerUser);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (reference != null && valueEventListenerUser != null) {
            reference.removeEventListener(valueEventListenerUser);
        }
        valueEventListenerUser = null;
        reference = null;
    }
}
