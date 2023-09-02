package com.clinic.management.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.clinic.management.app.R;
import com.clinic.management.app.databinding.ActivityRegistrationBinding;
import com.clinic.management.app.helper.LocaleHelper;
import com.clinic.management.app.helper.QuickbloxHelper;
import com.clinic.management.app.models.Constants;
import com.clinic.management.app.models.User;
import com.clinic.management.app.presenters.UsersCallback;
import com.clinic.management.app.presenters.UsersPresenter;
import com.clinic.management.app.utils.UiUtils;

public class RegistrationActivity extends BaseActivity implements UsersCallback, QuickbloxHelper.OnQuickbloxActionCallback {
    private ActivityRegistrationBinding binding;

    private UsersPresenter presenter;
    private User user;
    private int accountType;

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accountType = getIntent().getIntExtra(Constants.ARG_OBJECT, Constants.ACCOUNT_TYPE_CLIENT);
        binding.headerSub.setText(UiUtils.getAccountType(accountType));

        presenter = new UsersPresenter(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.password.getText().toString().trim();
                String confirmPassword = binding.rePassword.getText().toString().trim();
                String username = binding.username.getText().toString().trim();
                String phone = binding.phone.getText().toString().trim();
                String address = binding.address.getText().toString().trim();
                String fullName = binding.fullName.getText().toString().trim();

                if (username.isEmpty()) {
                    binding.username.setError(getString(R.string.str_username_invalid));
                    binding.username.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    binding.username.setError(getString(R.string.str_username_invalid));
                    binding.username.requestFocus();
                    return;
                }
                if (password.isEmpty() || password.length() < 8) {
                    binding.password.setError(getString(R.string.str_password_length_invalid));
                    binding.password.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
                    binding.password.setError(getString(R.string.str_password_confirm_invalid));
                    binding.password.requestFocus();
                    return;
                }
                if (fullName.isEmpty()) {
                    binding.phone.setError(getString(R.string.str_full_name_invalid));
                    binding.phone.requestFocus();
                    return;
                }
                if (phone.isEmpty()) {
                    binding.fullName.setError(getString(R.string.str_phone_invalid));
                    binding.fullName.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    binding.address.setError(getString(R.string.str_address_invalid));
                    binding.address.requestFocus();
                    return;
                }
                user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setFullName(fullName);
                user.setPhone(phone);
                user.setAddress(address);
                user.setAccountType(accountType);

                presenter.signup(user);
            }
        });
    }

    @Override
    public void onGetSignupUserComplete() {
        QuickbloxHelper helper = QuickbloxHelper.getInstance(this);
        helper.signUp(this, helper.createCurrentUser(user.getUsername(), user.getPassword(), user.getFullName()));
    }

    @Override
    public void onGetSignupUserFail(String message) {
        Toast.makeText(this, getString(R.string.str_signup_fail, message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_signup_fail, message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHideLoading() {
        hideProgressBar();
    }

    @Override
    public void onShowLoading() {
        showProgressBar();
    }

    private void hideProgressBar() {
        if (binding.progressBar2 != null) {
            binding.progressBar2.setVisibility(View.INVISIBLE);
        }
    }

    private void showProgressBar() {
        if (binding.progressBar2 != null) {
            binding.progressBar2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShowProgressDialog(int messageResourceId) {
        onShowLoading();
    }

    @Override
    public void onHideProgressDialog() {
        onHideLoading();
    }

    @Override
    public void onSingInComplete() {

    }

    @Override
    public void onSingUpComplete() {
        Toast.makeText(RegistrationActivity.this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        LoginActivity.start(this);
        finishAffinity();
    }
}