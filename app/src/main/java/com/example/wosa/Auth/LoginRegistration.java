package com.example.wosa.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wosa.Home.Home_Page_Drawer;
import com.example.wosa.R;

public class LoginRegistration extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Login");

        if (new UserLoginPref(getApplicationContext()).getPhone() != ""){
            startActivity(new Intent(LoginRegistration.this, Home_Page_Drawer.class));
            finish();
        }
        else {
            VerifyNumber verifyNumber = new VerifyNumber();
            replaceFragment(verifyNumber);
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLoginRegistration,fragment);
        ft.commit();
    }
}