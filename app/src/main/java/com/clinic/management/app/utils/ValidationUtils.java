package com.clinic.management.app.utils;

import static com.clinic.management.app.models.Constants.MAX_DISPLAY_NAME_LENGTH;
import static com.clinic.management.app.models.Constants.MAX_LOGIN_LENGTH;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    private ValidationUtils() {
    }

    private static boolean checkTextByPattern(String text, Pattern pattern) {
        boolean isCorrect = false;

        if (!TextUtils.isEmpty(text)) {
            Matcher matcher = pattern.matcher(text.trim());
            isCorrect = matcher.matches();
        }
        return isCorrect;
    }

    public static boolean isDisplayNameValid(String displayName) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z 0-9]{2," + (MAX_DISPLAY_NAME_LENGTH - 1) + "}+$");
        return checkTextByPattern(displayName, pattern);
    }

    public static boolean isLoginValid(String login) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]{2," + (MAX_LOGIN_LENGTH - 1) + "}+$");
        return checkTextByPattern(login, pattern);
    }
}