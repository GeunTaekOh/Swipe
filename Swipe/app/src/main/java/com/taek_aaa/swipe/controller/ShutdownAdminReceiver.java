package com.taek_aaa.swipe.controller;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by taek_aaa on 2017. 3. 1..
 */

public class ShutdownAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "관리자 권한을 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, "관리자 권한을 받았습니다.", Toast.LENGTH_SHORT).show();
    }
}
