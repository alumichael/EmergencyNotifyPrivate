package com.mikelearning.projecttest.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.EmergencyChatApp;
import com.mikelearning.projecttest.NotificationHome;
import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.Utils.UserPreferences;
import com.mikelearning.projecttest.events.PushNotificationEvent;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        String string_token = "Refreshed token: " + token;
        Log.d(TAG, string_token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(final String token) {
        UserPreferences sharedPrefUtil = new UserPreferences(getApplicationContext());
        String str = Constant.ARG_FIREBASE_TOKEN;
        sharedPrefUtil.saveString(str, token);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if(sharedPrefUtil.getUserType().equals("Emergency-Office")){
                FirebaseDatabase.getInstance().getReference().child(Constant.ARG_ORGS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .child(str)
                        .setValue(token);
            }else{
                FirebaseDatabase.getInstance().getReference().child(Constant.ARG_USERS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .child(str)
                        .setValue(token);
            }

        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String title = remoteMessage.getData().get(Constant.POST_TILE);
            String message = remoteMessage.getData().get("text");
            String email = remoteMessage.getData().get("email");
            String fcmToken = remoteMessage.getData().get("fcm_token");
            String firstname = remoteMessage.getData().get("firstname");
            String lastname = remoteMessage.getData().get("lastname");
            String next_of_kin_num = remoteMessage.getData().get("next_kin_num");

            if (!EmergencyChatApp.isNotificationActivityOpen()) {

                sendNotification(
                        firstname,
                        lastname,
                        next_of_kin_num,
                        title,
                        message,
                        email,
                        fcmToken);

            }else {
                new PushNotificationEvent(
                        firstname,
                        lastname,
                        next_of_kin_num,
                        title,
                        message,
                        email,
                        fcmToken);
            }


        }
    }

    private void sendNotification(String firstname,String lastname,String nextof_kin_num,String title,String message,String email,String firebaseToken) {

        Intent intent = new Intent(this, NotificationHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"channel_emergency")
                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                .setContentTitle(title)
                .setContentText(message+"\n"+"Name: "+firstname+" "+lastname+"\n"+"Next of Kin No: "+nextof_kin_num)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());


    }
}
