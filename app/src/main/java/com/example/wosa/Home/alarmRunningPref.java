package com.example.wosa.Home;

import android.content.Context;
import android.content.SharedPreferences;

public class alarmRunningPref {
    Context context;
    SharedPreferences sharedPreferences;
    boolean isAlarmRunning = false;

    public boolean getAlarmRunning() {
        isAlarmRunning = sharedPreferences.getBoolean("isAlarmRunning",false);
        return isAlarmRunning;
    }

    public void setAlarmRunning(boolean alarmRunning) {
        sharedPreferences.edit().putBoolean("isAlarmRunning",alarmRunning).commit();
    }

    public alarmRunningPref(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("alarmRunningPref",Context.MODE_PRIVATE);
    }



}
