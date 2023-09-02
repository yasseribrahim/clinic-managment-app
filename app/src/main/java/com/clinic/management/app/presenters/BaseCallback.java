package com.clinic.management.app.presenters;

import android.view.View;

public interface BaseCallback {
    void onFailure(String message, View.OnClickListener listener);
    void onShowLoading();
    void onHideLoading();
}
