package com.taek_aaa.swipe;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

import static com.taek_aaa.swipe.MainActivity.isStart;

public class SwipeSensorService extends Service {

    SensorManager sensorManager;
    Sensor sensor;

    public SwipeSensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();

    }
    @Override
    public void onDestroy() {
        //  Log.e("dhrms", "destroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    try {

                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }
/*
    protected void startSensor(){
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //Sensormanager를 이용해서 근접 센서 객체를 얻는다.
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


    }
    public void buttonClickTimeStart(){
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void buttonClickTimeStop(){
        //센서 값이 필요하지 않는 시점에 리스너를 해제해준다.
        sensorManager.unregisterListener(this);
    }*/
}
