package com.example.wosa.Auth;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLoginPref {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String Phone;
    private String countryCode;

    public String getPhone() {
        String mobile = sharedPreferences.getString("Phone","");
        return mobile;
    }

    public void setPhone(String phone) {
        sharedPreferences.edit().putString("Phone",phone).commit();
    }

    public UserLoginPref(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("UserLoginPref", Context.MODE_PRIVATE);
    }

    public String getCountryCode() {
        countryCode = sharedPreferences.getString("countryCode","");
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        sharedPreferences.edit().putString("countryCode",countryCode).commit();
    }

    public void logOutUser(){
        sharedPreferences.edit().clear();
        setPhone("");
    }
}
