package com.taek_aaa.swipe;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    public static Sensor sensor;
    public static SensorManager sensorManager;
    public static PowerManager.WakeLock wakeLock;
    public static DevicePolicyManager devicePolicyManager;
    public static SwitchCompat sensorSwitch;
    DataController dataController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //권한 받아오기
        getAuthority();

        //맨위상태바색상변경
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.headColor));
        }
        startSensor();

        sensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar.make(buttonView, "Swipe를 실행합니다.", Snackbar.LENGTH_LONG)
                            .setAction("ACTION", null).show();
                    Intent timerIntent = new Intent(MainActivity.this, SwipeSensorService.class);
                    dataController.setPreferencesIsStart(getBaseContext(),1);
                    startService(timerIntent);

                } else {
                    Snackbar.make(buttonView, "Swipe를 종료합니다.", Snackbar.LENGTH_LONG)
                            .setAction("ACTION", null).show();
                    Intent timerIntent = new Intent(MainActivity.this, SwipeSensorService.class);
                    dataController.setPreferencesIsStart(getBaseContext(),0);
                    stopService(timerIntent);
                }
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(sensorSwitch.isChecked()==false){
            sensor = null;
            sensorManager=null;
        }
    }
    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataController = new DataController();


        sensorSwitch = (SwitchCompat) findViewById(R.id.switchButton);
        if(dataController.getPreferencesIsStart(getBaseContext())==0) {
            sensorSwitch.setChecked(false);
        }else{
            sensorSwitch.setChecked(true);
        }

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

    private void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getClass().getName());

        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

}
