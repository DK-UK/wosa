package com.example.wosa.Recepients_fragment;

import android.content.Context;
import android.content.SharedPreferences;

public class phoneDetail {
    Context context;
    String name1,number1;


    String  name2,number2,
            name3,number3,
            name4,number4,
            name5,number5;

    SharedPreferences sharedPreferences;

    public phoneDetail(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("phoneDetail",Context.MODE_PRIVATE);
    }

    public String getName1() {
        name1 = sharedPreferences.getString("name1","");
        return name1;
    }

    public void setName1(String name1) {
        sharedPreferences.edit().putString("name1",name1).commit();
    }

    public String getNumber1() {
        number1 = sharedPreferences.getString("number1","");
        return number1;
    }

    public void setNumber1(String number) {
        sharedPreferences.edit().putString("number1",number).commit();
    }
    public void clear(){
        sharedPreferences.edit().clear().commit();
    }
    public String getName2() {
        name2 = sharedPreferences.getString("name2","");
        return name2;
    }

    public void setName2(String name2) {
        sharedPreferences.edit().putString("name2",name2).commit();
    }

    public String getNumber2() {
        number2 = sharedPreferences.getString("number2","");
        return number2;
    }

    public void setNumber2(String number2) {
        sharedPreferences.edit().putString("number2",number2).commit();
    }

    public String getName3() {
        name3 = sharedPreferences.getString("name3","");
        return name3;
    }

    public void setName3(String name3) {
        sharedPreferences.edit().putString("name3",name3).commit();
    }

    public String getNumber3() {
        number3 = sharedPreferences.getString("number3","");
        return number3;
    }

    public void setNumber3(String number3) {
        sharedPreferences.edit().putString("number3",number3).commit();
    }

    public String getName4() {
        name4 = sharedPreferences.getString("name4","");
        return name4;
    }

    public void setName4(String name4) {
        sharedPreferences.edit().putString("name4",name4).commit();
    }

    public String getNumber4() {
        number4 = sharedPreferences.getString("number4","");
        return number4;
    }

    public void setNumber4(String number4) {
        sharedPreferences.edit().putString("number4",number4).commit();
    }

    public String getName5() {
        name5 = sharedPreferences.getString("name5","");
        return name5;
    }

    public void setName5(String name5) {
        sharedPreferences.edit().putString("name5",name5).commit();
    }

    public String getNumber5() {
        number5 = sharedPreferences.getString("number5","");
        return number5;
    }

    public void setNumber5(String number5) {
        sharedPreferences.edit().putString("number5",number5).commit();
    }
    public void clearRecepients(){
        sharedPreferences.edit().clear().commit();
    }
}
