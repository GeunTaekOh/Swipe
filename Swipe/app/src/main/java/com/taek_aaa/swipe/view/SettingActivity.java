package com.taek_aaa.swipe.view;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.taek_aaa.swipe.R;

import static com.taek_aaa.swipe.view.MainActivity.headColorThem;


/**
 * Created by taek_aaa on 2017. 3. 3..
 */

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(headColorThem);
        }
    }
    public void onClickBackSpace(View v) {
        onBackPressed();
    }


}
