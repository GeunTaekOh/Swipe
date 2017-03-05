package com.taek_aaa.swipe.controller;

import android.app.Notification;
import android.app.NotificationManager;

import com.taek_aaa.swipe.R;

/**
 * Created by taek_aaa on 2017. 3. 4..
 */

public class NotificationController {
    NotificationManager nm;
    Notification.Builder builder;

    /*public void init(){
        nm = new NotificationManager()
    }*/



    public void startNotification(NotificationManager nm, Notification.Builder builder){
        builder.setSmallIcon(R.drawable.icon2);
        builder.setTicker("Sample");
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(10);
        builder.setContentTitle("Swipe가 실행중입니다.");
        builder.setContentText("Swipe On/Off");
        Notification noti = builder.build();
        noti.flags = noti.FLAG_NO_CLEAR;
        nm.notify(1, noti);

    }

}
