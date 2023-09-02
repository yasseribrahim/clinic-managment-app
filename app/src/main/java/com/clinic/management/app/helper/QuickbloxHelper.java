package com.clinic.management.app.helper;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.clinic.management.app.ClinicManagementApplication;
import com.clinic.management.app.R;
import com.clinic.management.app.models.Constants;
import com.clinic.management.app.services.LoginService;
import com.clinic.management.app.ui.activities.LoginActivity;
import com.clinic.management.app.ui.activities.OpponentsActivity;
import com.clinic.management.app.utils.QBEntityCallbackImpl;
import com.clinic.management.app.utils.QBResRequestExecutor;
import com.clinic.management.app.utils.SharedPrefsHelper;
import com.clinic.management.app.utils.ToastUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class QuickbloxHelper {
    private static final QuickbloxHelper HELPER = new QuickbloxHelper();
    private String TAG = LoginActivity.class.getSimpleName();
    private OnQuickbloxActionCallback callback;
    private QBResRequestExecutor requestExecutor;
    private SharedPrefsHelper sharedPrefsHelper;

    private QBUser currentUser;

    private QuickbloxHelper() {
        this.requestExecutor = ClinicManagementApplication.getInstance().getQbResRequestExecutor();
        this.sharedPrefsHelper = SharedPrefsHelper.getInstance();
    }

    public static QuickbloxHelper getInstance(OnQuickbloxActionCallback callback) {
        HELPER.callback = callback;
        return HELPER;
    }

    public void signUp(Activity activity, QBUser user) {
        Log.d(TAG, "SignUp New User");
        if (callback != null) {
            callback.onShowProgressDialog(R.string.dlg_creating_new_user);
        }
        requestExecutor.signUpNewUser(user, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        Log.d(TAG, "SignUp Successful");
                        saveUser(user);
                        if (callback != null) {
                            callback.onHideProgressDialog();
                            callback.onSingUpComplete();
                        }
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Error SignUp" + e.getMessage());
                        if (e.getHttpStatusCode() == Constants.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signIn(activity, user);
                        } else {
                            if (callback != null) {
                                callback.onHideProgressDialog();
                            }
                            ToastUtils.longToast(R.string.sign_up_error);
                        }
                    }
                }
        );
    }

    public void loginToChat(Activity activity, QBUser user) {
        user.setPassword(ClinicManagementApplication.USER_DEFAULT_PASSWORD);
        currentUser = user;
        startLoginService(activity, user);
    }

    public void saveUser(QBUser user) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveUser(user);
    }

    public QBUser createCurrentUser(String userLogin, String password, String userFullName) {
        QBUser user = null;
        if (!TextUtils.isEmpty(userLogin) && !TextUtils.isEmpty(userFullName)) {
            user = new QBUser();
            user.setLogin(userLogin);
            user.setFullName(userFullName);
            user.setPassword(password);
        }
        return user;
    }

    public void signIn(Activity activity, QBUser user) {
        Log.d(TAG, "SignIn Started");
        if (callback != null) {
            callback.onShowProgressDialog(R.string.dlg_creating_new_user);
        }
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {
                Log.d(TAG, "SignIn Successful");
                sharedPrefsHelper.saveUser(currentUser);
                updateUserOnServer(activity, user);
                if (callback != null) {
                    callback.onHideProgressDialog();
                    callback.onSingInComplete();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.d(TAG, "Error SignIn" + responseException.getMessage());
                if (callback != null) {
                    callback.onHideProgressDialog();
                }
                ToastUtils.longToast(R.string.sign_in_error);
            }
        });
    }

    public void updateUserOnServer(Activity activity, QBUser user) {
        user.setPassword(null);
        QBUsers.updateUser(user).performAsync(new QBEntityCallback<>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                if (callback != null) {
                    callback.onHideProgressDialog();
                }
                OpponentsActivity.start(activity);
                activity.finish();
            }

            @Override
            public void onError(QBResponseException e) {
                if (callback != null) {
                    callback.onHideProgressDialog();
                }
                ToastUtils.longToast(R.string.update_user_error);
            }
        });
    }

    public void startLoginService(Activity activity, QBUser user) {
        Intent intent = new Intent(activity, LoginService.class);
        PendingIntent pendingIntent = activity.createPendingResult(Constants.EXTRA_LOGIN_RESULT_CODE, intent, 0);
        LoginService.start(activity, user, pendingIntent);
    }

    public interface OnQuickbloxActionCallback {
        void onShowProgressDialog(int messageResourceId);

        void onHideProgressDialog();

        void onSingInComplete();

        void onSingUpComplete();
    }
}
