package com.clinic.management.app.utils;

import android.widget.Toast;

import androidx.annotation.StringRes;

import com.clinic.management.app.ClinicManagementApplication;

public class ToastUtils {

    private ToastUtils() {
        //empty
    }

    public static void shortToast(String message) {
        show(message, Toast.LENGTH_LONG);
    }

    public static void shortToast(@StringRes int resource) {
        show(ClinicManagementApplication.getInstance().getString(resource), Toast.LENGTH_SHORT);
    }

    public static void longToast(String message) {
        show(message, Toast.LENGTH_LONG);
    }

    public static void longToast(@StringRes int resource) {
        show(ClinicManagementApplication.getInstance().getString(resource), Toast.LENGTH_LONG);
    }

    private static void show(String message, int length) {
        Toast.makeText(ClinicManagementApplication.getInstance(), message, length).show();
    }
}