package com.mikelearning.projecttest.fcm;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmNotificationBuilder {

    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_KEY = "key=AAAAMDEx_TE:APA91bFOaAPap5PIwhEE_Uuj93_dghpvuId8OhQqbqvksNJ_KaNVd6RHUpQk9Gv5fsZlR-2LXtsBnZ_t04xzUCvSrYvUbieCU0JGHg7LJqlCzlb82MIjVrzOHg8jKlv05uzIf4hkL3Xx";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String KEY_DATA = "data";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_FirstNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_NEXT_OF_KIN_NUM= "next_kin_num";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TO = "to";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SERVER_API_KEY = "AAAAMDEx_TE:APA91bFOaAPap5PIwhEE_Uuj93_dghpvuId8OhQqbqvksNJ_KaNVd6RHUpQk9Gv5fsZlR-2LXtsBnZ_t04xzUCvSrYvUbieCU0JGHg7LJqlCzlb82MIjVrzOHg8jKlv05uzIf4hkL3Xx";
    private static final String TAG = "FcmNotificationBuilder";
    private String mEmail;
    private String mFirebaseToken;
    private String mFirstname;
    private String mLastname;
    private String mNextKinNum;
    private String mMessage;
    private String mReceiverFirebaseToken;
    private String mTitle;


    private FcmNotificationBuilder() {
    }

    public static FcmNotificationBuilder initialize() {
        return new FcmNotificationBuilder();
    }

    public FcmNotificationBuilder title(String title) {
        this.mTitle = title;
        return this;
    }

    public FcmNotificationBuilder message(String message) {
        this.mMessage = message;
        return this;
    }

    public FcmNotificationBuilder firstname(String firstname) {
        this.mFirstname = firstname;
        return this;
    }

    public FcmNotificationBuilder lastname(String lastname) {
        this.mLastname = lastname;
        return this;
    }

    public FcmNotificationBuilder next_kin_num(String nextkinNum) {
        this.mNextKinNum = nextkinNum;
        return this;
    }

    public FcmNotificationBuilder email(String email) {
        this.mEmail = email;
        return this;
    }


    public FcmNotificationBuilder firebaseToken(String firebaseToken) {
        this.mFirebaseToken = firebaseToken;
        return this;
    }

    public FcmNotificationBuilder receiverFirebaseToken(String receiverFirebaseToken) {
        this.mReceiverFirebaseToken = receiverFirebaseToken;
        return this;
    }

    public void send() {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Request request = new Request.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, AUTH_KEY)
                .url(FCM_URL)
                .post(requestBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onGetAllUsersFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });

    }

    private JSONObject getValidJsonBody() throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put(KEY_TO, mReceiverFirebaseToken);
        //Data
        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put(KEY_TITLE, mTitle);
        jsonObjectData.put(KEY_FirstNAME, mFirstname);
        jsonObjectData.put(KEY_LASTNAME, mLastname);
        jsonObjectData.put(KEY_NEXT_OF_KIN_NUM, mNextKinNum);
        jsonObjectData.put(KEY_TEXT, mMessage);
        jsonObjectData.put(KEY_EMAIL, mEmail);
        jsonObjectData.put(KEY_FCM_TOKEN, mFirebaseToken);
        jsonObjectBody.put(KEY_DATA, jsonObjectData);
        return jsonObjectBody;
    }
}
