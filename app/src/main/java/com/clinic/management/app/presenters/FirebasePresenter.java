package com.clinic.management.app.presenters;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.clinic.management.app.ClinicManagementApplication;
import com.clinic.management.app.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebasePresenter {
    private final static String URL = "https://fcm.googleapis.com/fcm/send";
    private final static String KEY = "key=" + "AAAAFgZfEBA:APA91bFQOgKSdIKBs2pshHpGYO4OOmhfVXPdWjfpzgAcu7Ls0z2uEybcf7ztHUR4wFmbFVyLhIjX5DzbINXDwxV5azOB1aLbFmmbeZ1ZFjKrzZfSX4C-9QMw736WoZoB12n6rjLqpALf";
    private final static String CONTENT_TYPE = "application/json";
    private FirebaseCallback callback;
    private RequestQueue requestQueue;

    public FirebasePresenter(FirebaseCallback callback) {
        this.callback = callback;
        this.requestQueue = Volley.newRequestQueue(ClinicManagementApplication.getInstance().getApplicationContext());
    }

    public void send(Message message, List<String> tokens) {
        JSONObject notification = new JSONObject();
        JSONObject notificationContent = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("title", message.getSenderName());
            data.put("message", message.getMessage());
            notificationContent.put("title", message.getSenderName());
            notificationContent.put("body", message.getMessage());
            if (tokens != null && tokens.size() > 0) {
                JSONArray registrationIds = new JSONArray();
                for (int i = 0; i < tokens.size(); i++) {
                    registrationIds.put(tokens.get(i));
                }
                notification.put("registration_ids", registrationIds);
            }
            notification.put("data", data);
            notification.put("notification", notificationContent);
            Log.e("TAG", "try");
            Log.e("TAG", notification.toString());
        } catch (JSONException ex) {
            Log.e("TAG", "onCreate: " + ex.getMessage());
        }

        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        Log.e("TAG", "sendNotification");

        JsonObjectRequest request = new JsonObjectRequest(URL, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("TAG", response.toString());

                if (callback != null) {
                    callback.onSendNotificationComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ClinicManagementApplication.getInstance().getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();

                if (callback != null) {
                    callback.onSendNotificationComplete();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", KEY);
                params.put("Content-Type", CONTENT_TYPE);
                return params;
            }
        };
        requestQueue.add(request);
    }
}
