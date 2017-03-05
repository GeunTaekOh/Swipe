package com.taek_aaa.swipe.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.widget.RemoteViews;

import com.taek_aaa.swipe.R;

/**
 * Created by taek_aaa on 2017. 3. 4..
 */

public class NotificationController {
    NotificationManager nm;
    Notification.Builder builder;



    public void startNotification(NotificationManager nm, Notification.Builder builder, PendingIntent pi, RemoteViews contentView){
        this.nm = nm;
        this.builder = builder;
        contentView.setImageViewResource(R.id.image, R.drawable.icon2);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");
        builder.setSmallIcon(R.drawable.icon2);
        builder.setTicker("Sample");
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(10);
        builder.setContentTitle("Swipe가 실행중입니다.");
        builder.setContentText("Swipe On/Off");
        builder.setContentIntent(pi);
        builder.setContent(contentView);
        Notification noti = builder.build();

        noti.flags = noti.FLAG_NO_CLEAR;
        //noti.contentView = contentView;
        nm.notify(1, noti);
    }

    public NotificationManager getNotificationManager(){
        return this.nm;
    }
    public Notification.Builder getBuilder(){
        return this.builder;
    }
    public void setNotificationManager (NotificationManager nm){
        this.nm = nm;
    }
    public void setBuilder(Notification.Builder builder){
        this.builder = builder;
    }


}
