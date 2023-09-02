package com.clinic.management.app.ui.activities;

import android.os.Bundle;
import android.view.View;

import com.clinic.management.app.R;
import com.clinic.management.app.databinding.ActivitySelectAccountTypeBinding;
import com.clinic.management.app.models.Constants;

public class SelectAccountTypeActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySelectAccountTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectAccountTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAccountClient.setOnClickListener(this);
        binding.btnAccountDoctor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_account_client -> openRegisterActivity(Constants.ACCOUNT_TYPE_CLIENT);
            case R.id.btn_account_doctor -> openRegisterActivity(Constants.ACCOUNT_TYPE_DOCTOR);
        }
    }

    private void openRegisterActivity(int type) {
        RegistrationActivity.start(this, type);
    }
}