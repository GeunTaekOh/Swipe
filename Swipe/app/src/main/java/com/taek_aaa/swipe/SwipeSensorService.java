package com.taek_aaa.swipe;

import android.app.KeyguardManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import static com.taek_aaa.swipe.MainActivity.devicePolicyManager;
import static com.taek_aaa.swipe.MainActivity.sensor;
import static com.taek_aaa.swipe.MainActivity.sensorManager;
import static com.taek_aaa.swipe.MainActivity.wakeLock;

public class SwipeSensorService extends Service implements SensorEventListener {

    Boolean isWakeup = false;
    SensorEvent event;

    public SwipeSensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public void onDestroy() {
        buttonClickTimeStop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buttonClickTimeStart();
        return START_STICKY;
    }

    public void buttonClickTimeStart() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void buttonClickTimeStop() {
        //센서 값이 필요하지 않는 시점에 리스너를 해제해준다.
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        this.event = sensorEvent;

        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (sensorEvent.values[0] >= -0.01 && sensorEvent.values[0] <= 0.01) {
                //센서가까울떄
                ComponentName comp = new ComponentName(this, ShutdownAdminReceiver.class);

                devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                if (!devicePolicyManager.isAdminActive(comp)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, comp);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "message string");
                    startActivity(intent);
                } else {

                    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                    if (!isWakeup && keyguardManager.inKeyguardRestrictedInputMode()) {
                        // lock screen
                        acquireWakeLock(this);
                        isWakeup = true;
                        Log.e("test", "화면킴");

                    } else {
                        // lock screen 이 아님
                        devicePolicyManager.lockNow();
                        devicePolicyManager = null;
                        isWakeup = false;
                        Log.e("test", "화면끔");
                    }
                }
            } else if (sensorEvent.values[0] <= -0.01 || sensorEvent.values[0] >= 0.01) {
                //멀어젔을떄
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getClass().getName());
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }
}
