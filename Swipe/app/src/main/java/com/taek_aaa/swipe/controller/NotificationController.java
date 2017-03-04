package com.taek_aaa.swipe.controller;

import android.app.Notification;
import android.app.NotificationManager;

import com.taek_aaa.swipe.R;

/**
 * Created by taek_aaa on 2017. 3. 4..
 */

public class NotificationController {;

    public void startNotification(NotificationManager nm, Notification.Builder builder){
        builder.setSmallIcon(R.drawable.icon);
        builder.setTicker("Sample");
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(10);
        builder.setContentTitle("Title");
        builder.setContentText("");
        Notification noti = builder.build();
        nm.notify(1, noti);

    }

}
