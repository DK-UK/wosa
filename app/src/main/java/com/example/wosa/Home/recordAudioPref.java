package com.example.wosa.Home;

import android.content.Context;
import android.content.SharedPreferences;

public class recordAudioPref {
    Context context;
    SharedPreferences sharedPreferences;
    String audioUrl;
    boolean onlyFor1stTime;


    public recordAudioPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("audioPref",Context.MODE_PRIVATE);
    }

    public boolean isOnlyFor1stTime() {
        onlyFor1stTime = sharedPreferences.getBoolean("onlyFor1stTime",false);
        return onlyFor1stTime;
    }

    public void setOnlyFor1stTime(boolean onlyFor1stTime) {
        sharedPreferences.edit().putBoolean("onlyFor1stTime",onlyFor1stTime).commit();
    }

    public String getAudioUrl() {
        audioUrl = sharedPreferences.getString("audioUrl","");
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        sharedPreferences.edit().putString("audioUrl",audioUrl).commit();
    }

}
