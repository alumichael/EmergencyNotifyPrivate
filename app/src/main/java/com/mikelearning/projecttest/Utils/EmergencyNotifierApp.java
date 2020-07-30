package com.mikelearning.projecttest.Utils;

import android.app.Application;

public class EmergencyNotifierApp extends Application {
    private static boolean sIsChatActivityOpen = false;

    public static boolean isFeedbackActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setFeedbackActivityOpen(boolean isChatActivityOpen) {
        sIsChatActivityOpen = isChatActivityOpen;
    }

    public void onCreate() {
        super.onCreate();
    }
}
