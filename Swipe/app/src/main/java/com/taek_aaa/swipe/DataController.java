package com.taek_aaa.swipe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by taek_aaa on 2017. 3. 3..
 */

public class DataController extends Activity{


    /** preference 값 가져오기 **/
    public int getPreferencesIsStart(Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        int a;
        a = pref.getInt("isStart", 0);
        return a;
    }

    /** preference 인자값 으로 저장하기 **/
    public void setPreferencesIsStart(Context context, int a) {
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isStart", a);
        editor.commit();
    }

}
