package com.clinic.management.app.presenters;

public interface FirebaseCallback extends BaseCallback {
    default void onSendNotificationComplete() {
    }
}
