package com.example.wosa.Profile_fragments;

import android.content.Context;
import android.content.SharedPreferences;

public class profile_ipClass {
    String name;
    String age;
    String address;
    SharedPreferences sharedPreferences;
    boolean registeredUser;

    public boolean getRegisteredUser() {
        registeredUser = sharedPreferences.getBoolean("registered",false);
        return registeredUser;
    }

    public void setRegisteredUser(boolean registeredUser) {
        this.registeredUser = registeredUser;
        sharedPreferences.edit().putBoolean("registered",registeredUser).commit();
    }

    Context context;
    public profile_ipClass(Context cxt){
        context = cxt;
        sharedPreferences = cxt.getSharedPreferences("save",Context.MODE_PRIVATE);

    }


    public String getName() {
         name = sharedPreferences.getString("name","");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("name",name).commit();
    }

    public String getAge() {
        age = sharedPreferences.getString("age","");
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        sharedPreferences.edit().putString("age",age).commit();
    }

    public String getAddress()
    {
        address = sharedPreferences.getString("address","");
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        sharedPreferences.edit().putString("address",address).commit();
    }



    public void clearProfile(){
        sharedPreferences.edit().clear().commit();
    }


}
