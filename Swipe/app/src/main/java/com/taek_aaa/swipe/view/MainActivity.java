package com.taek_aaa.swipe.view;

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

import com.taek_aaa.swipe.R;
import com.taek_aaa.swipe.SwipeSensorService;
import com.taek_aaa.swipe.controller.DataController;
import com.taek_aaa.swipe.controller.ShutdownAdminReceiver;

import static com.taek_aaa.swipe.R.color.headColor;

public class MainActivity extends AppCompatActivity {

    public static Sensor sensor;
    public static SensorManager sensorManager;
    public static PowerManager.WakeLock wakeLock;
    public static DevicePolicyManager devicePolicyManager;
    public static SwitchCompat sensorSwitch;
    public static int headColorThem;
    DataController dataController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        headColorThem = getResources().getColor(headColor);
        getAuthority();
        init();

        //맨위상태바색상변경
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(headColorThem);
        }
        startSensor();
        ComponentName comp = new ComponentName(this, ShutdownAdminReceiver.class);

        devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!devicePolicyManager.isAdminActive(comp)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, comp);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "message string");
            startActivityForResult(intent, 101);
        }

        sensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent timerIntent = new Intent(MainActivity.this, SwipeSensorService.class);

                if (isChecked) {
                    Snackbar.make(buttonView, "Swipe를 실행합니다.", Snackbar.LENGTH_LONG).setAction("ACTION", null).show();
                    dataController.setPreferencesIsStart(getBaseContext(), 1);
                    startService(timerIntent);
                } else {
                    Snackbar.make(buttonView, "Swipe를 종료합니다.", Snackbar.LENGTH_LONG).setAction("ACTION", null).show();
                    dataController.setPreferencesIsStart(getBaseContext(), 0);
                    stopService(timerIntent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        switch (dataController.getPreferencesIsStart(getBaseContext())) {
            case 0:
                sensorSwitch.setChecked(false);
                break;
            case 1:
                sensorSwitch.setChecked(true);
                break;
        }

    }

    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataController = new DataController();
        sensorSwitch = (SwitchCompat) findViewById(R.id.switchButton);

        switch (dataController.getPreferencesIsStart(getBaseContext())) {
            case 0:
                sensorSwitch.setChecked(false);
                break;
            case 1:
                sensorSwitch.setChecked(true);
                break;
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.howToUse:

                break;
            case R.id.contactUs:
                ContactUsDialog contactUsDialog = new ContactUsDialog(this);
                contactUsDialog.show();
                break;
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
