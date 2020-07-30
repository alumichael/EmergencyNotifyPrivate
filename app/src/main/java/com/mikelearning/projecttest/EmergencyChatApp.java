package com.mikelearning.projecttest;

import android.app.Application;

public class EmergencyChatApp extends Application {
    private static boolean sINotificationActivityOpen = false;

    public static boolean isNotificationActivityOpen() {
        return sINotificationActivityOpen;
    }

    public static void setFeedbackActivityOpen(boolean isFeedbackActivityOpen) {
        sINotificationActivityOpen = isFeedbackActivityOpen;
    }

    public void onCreate() {
        super.onCreate();
    }
}
