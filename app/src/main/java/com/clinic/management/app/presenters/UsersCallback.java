package com.clinic.management.app.presenters;

import com.clinic.management.app.models.User;

import java.util.List;

public interface UsersCallback extends BaseCallback {
    default void onGetUsersComplete(List<User> users) {
    }

    default void onSaveUserComplete() {
    }

    default void onGetDeleteUserComplete(int position) {
    }

    default void onGetSignupUserComplete() {
    }

    default void onGetSignupUserFail(String message) {
    }

    default void onGetUserComplete(User user) {
    }

    default void onSaveTokenComplete() {
    }
}
