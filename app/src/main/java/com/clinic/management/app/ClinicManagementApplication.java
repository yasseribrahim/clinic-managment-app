package com.clinic.management.app;

import android.app.Application;

import com.clinic.management.app.utils.QBResRequestExecutor;
import com.quickblox.auth.session.QBSettings;

public class ClinicManagementApplication extends Application {
    // app credentials
    private static final String APPLICATION_ID = "101662";
    private static final String AUTH_KEY = "xP-wUSzgjVJ469R";
    private static final String AUTH_SECRET = "H-pJXjdxC9NdSq2";
    private static final String ACCOUNT_KEY = "u1uFMSf5bRZcz8smzGsb";

    public static final String USER_DEFAULT_PASSWORD = "12345678";

    private static ClinicManagementApplication instance;
    private QBResRequestExecutor qbResRequestExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        checkAppCredentials();
        initCredentials();
    }

    private void checkAppCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw new AssertionError(getString(R.string.error_credentials_empty));
        }
    }

    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        // uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your_api_endpoint.com", "your_chat_endpoint", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }

    public static ClinicManagementApplication getInstance() {
        return instance;
    }
}