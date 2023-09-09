package com.clinic.management.app.presenters;

import android.view.View;

public interface BaseCallback {
    default void onFailure(String message, View.OnClickListener listener) {
    }

    default void onShowLoading() {
    }

    default void onHideLoading() {
    }
}
