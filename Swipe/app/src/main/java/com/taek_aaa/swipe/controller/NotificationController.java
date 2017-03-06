package com.taek_aaa.swipe.controller;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.taek_aaa.swipe.R;
import com.taek_aaa.swipe.SwipeSensorService;
import com.taek_aaa.swipe.view.MainActivity;

import static com.taek_aaa.swipe.view.MainActivity.sensorManager;

/**
 * Created by taek_aaa on 2017. 3. 4..
 */

public class NotificationController extends Activity {
    NotificationManager nm;
    Notification.Builder builder;
    Button exitBtn;
    static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notification);
        exitBtn = (Button) findViewById(R.id.notiExitButton);

    }

    private static void deleteService(){
        SharedPreferences pref = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isStart", 0);
        editor.commit();
    }

    public void startNotification(NotificationManager nm, Notification.Builder builder, PendingIntent pi, RemoteViews contentView, Context context) {
        this.nm = nm;
        this.builder = builder;
        this.mContext = context;
        contentView.setImageViewResource(R.id.image, R.drawable.icon2);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");
        contentView.setOnClickPendingIntent(R.id.notiExitButton, getPendingIntent(context, R.id.notiExitButton));

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

        Intent switchIntent = new Intent(context, switchButtonListener.class);
        switchIntent.setAction(Intent.ACTION_SEND);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, switchIntent, 0);
        contentView.setOnClickPendingIntent(R.id.notiExitButton, pendingSwitchIntent);

        nm.notify(1, noti);

    }

    private PendingIntent getPendingIntent(Context context, int id) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getBroadcast(context, id, intent, 0);
    }


    public static class switchButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {




            SwipeSensorService service = new SwipeSensorService();
            sensorManager.unregisterListener(service);

            deleteService();

            Toast.makeText(context,"test",Toast.LENGTH_SHORT).show();

        }
    }
    
}
