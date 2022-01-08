package com.soultalkproduction.wish;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class TriggerTaskAlarm extends Application {
    public static final String CHANNEL_ID = "Notify_Events";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Today's Events",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("See today's events");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}