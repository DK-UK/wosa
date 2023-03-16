package com.example.wosa;

import android.content.Context;
import android.content.SharedPreferences;

public class Buzzer_tone {
    SharedPreferences sharedPreferences;
    Context context;
    String BuzzerTone;

    public String getBuzzerTone() {
        BuzzerTone = sharedPreferences.getString("buzzer_tone","");
        return BuzzerTone;
    }

    public void setBuzzerTone(String buzzerTone) {
        sharedPreferences.edit().putString("buzzer_tone",buzzerTone).commit();
    }

    public Buzzer_tone(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("buzzer_tone",Context.MODE_PRIVATE);
    }

}
