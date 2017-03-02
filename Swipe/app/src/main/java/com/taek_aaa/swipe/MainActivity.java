package com.taek_aaa.swipe;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static Boolean isStart = false;
    Sensor sensor;
    SensorManager sensorManager;
    PowerManager.WakeLock wakeLock;
    DevicePolicyManager devicePolicyManager;
    SwitchCompat sensorSwitch;
    Boolean isWakeup=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        startSensor();

        //권한 받아오기
        getAuthority();

        //맨위상태바색상변경
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.headColor));
        }


        sensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar.make(buttonView, "Swipe를 실행합니다.", Snackbar.LENGTH_LONG)
                            .setAction("ACTION", null).show();
                    buttonClickTimeStart();

                } else {
                    Snackbar.make(buttonView, "Swipe를 종료합니다.", Snackbar.LENGTH_LONG)
                            .setAction("ACTION", null).show();
                    buttonClickTimeStop();
                }
            }
        });

    }

    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sensorSwitch = (SwitchCompat) findViewById(R.id.switchButton);
        sensorSwitch.setChecked(false);
    }

    protected void getAuthority() {
        devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), MainActivity.class);
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent, 0);
        }
        acquireWakeLock(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void startSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
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
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (sensorEvent.values[0] >= -0.01 && sensorEvent.values[0] <= 0.01) {
                //센서가까울떄

                Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
                ComponentName comp = new ComponentName(this, ShutdownAdminReceiver.class);

                devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                if (!devicePolicyManager.isAdminActive(comp)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, comp);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "message string");
                    startActivityForResult(intent, 101);
                    Log.e("test","device에서안됨");
                } else {
                    Log.e("test","else들어옴");
                    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                    Log.e("test","else아래들어옴");
                    if (!isWakeup && keyguardManager.inKeyguardRestrictedInputMode()) {
                        // lock screen
                        acquireWakeLock(this);
                        isWakeup=true;
                        Log.e("test","화면킴");

                    } else {
                        // lock screen 이 아님
                        devicePolicyManager.lockNow();
                        devicePolicyManager=null;
                        isWakeup=false;
                        Log.e("test","화면끔");
                    }


                }
            } else if (sensorEvent.values[0] <= -0.01 || sensorEvent.values[0] >= 0.01) {
                //멀어젔을떄
                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
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
