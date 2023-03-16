package com.example.wosa.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.wosa.Auth.LoginRegistration;
import com.example.wosa.Auth.UserLoginPref;
import com.example.wosa.Profile_fragments.Profile;
import com.example.wosa.Profile_fragments.profile_ipClass;
import com.example.wosa.R;
import com.example.wosa.Recepients_fragment.Recepients;
import com.example.wosa.Recepients_fragment.phoneDetail;
import com.example.wosa.menu_items.Self_Defense;
import com.example.wosa.menu_items.Settings;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class Home_Page_Drawer extends AppCompatActivity {
    public Home_Page_Drawer(){
    }
    Button buzzer_btn,panic_btn,safe_btn;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    static boolean doubleBackToExitPressedOnce = false;

    phoneDetail phoneDetail;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColor();
        setContentView(R.layout.activity_home__page__drawer);

        firebaseAuth = FirebaseAuth.getInstance();

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        panic_btn = findViewById(R.id.panic_btn);
        buzzer_btn = findViewById(R.id.buzzer_btn);
        safe_btn = findViewById(R.id.safe_btn);
        drawerLayout = findViewById(R.id.drawer);
        phoneDetail = new phoneDetail(getApplicationContext());

        final MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(Home_Page_Drawer.this);
        profile_ipClass profile_ipClass = new profile_ipClass(getApplicationContext());

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.navigation_drawer);
        try {
            View header_view = navigationView.getHeaderView(0);
            TextView header_text = header_view.findViewById(R.id.header_name);

            if (!profile_ipClass.getName().isEmpty()) {
                header_text.setText(profile_ipClass.getName());
            }
            else{
                header_text.setText("WOSA");
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext()," exception : "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.profile)
                {
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                    finish();
                }
                else if(id == R.id.recepient)
                {
                    startActivity(new Intent(getApplicationContext(), Recepients.class));
                    finish();
                }
                else if(id == R.id.self_defense){
                    startActivity(new Intent(getApplicationContext(), Self_Defense.class));
                    finish();
                }
                else if(id == R.id.settings) {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    finish();
                }
                else if(id == R.id.logout){
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Home_Page_Drawer.this);
                    builder.setTitle("Logout?")
                            .setMessage("Are you sure to logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new UserLoginPref(getApplicationContext()).logOutUser();
                                    startActivity(new Intent(getApplicationContext(), LoginRegistration.class));
                                    finish();
                                }
                            })
                    .setNegativeButton("No",null)
                    .show();

                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        final Record_Audio record_audio = new Record_Audio(getApplicationContext());
        //Panic Button to send Location Messages
        panic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.setIcon(R.drawable.ic_error_outline_black)
                dialog.setIcon(R.drawable.ic_message_black_24dp)
                        .setTitle("Location Messages ?")
                        .setMessage("This will send the Location contained text messages to your registered contacts")
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (phoneDetail.getNumber1() != "" && phoneDetail.getNumber2() != "" && phoneDetail.getNumber3() != ""
                                        && phoneDetail.getNumber4() != "" && phoneDetail.getNumber5() != "") {
                                    new alarmManager(getApplicationContext()).alarmStart(true);
                                    panic_btn.setVisibility(View.INVISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            safe_btn.setVisibility(View.VISIBLE);
                                        }
                                    },6000);
                                } else {
                                    Toast.makeText(Home_Page_Drawer.this, "please register the contacts", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Home_Page_Drawer.this,Recepients.class));
                                    finish();
                                }
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        setFullVolume();
        //Buzzer Button to pop up Sound for buzzer
        buzzer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setIcon(R.drawable.ic_error_outline_black)
                        .setTitle("Buzzer pop up ? ")
                        .setMessage("This will repeatedly playing the selected sound that going to be your helping hand")
                        .setPositiveButton("Try Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                record_audio.buzzer_Play(true);
                                safe_btn.setVisibility(View.VISIBLE);
                                buzzer_btn.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        safe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new alarmRunningPref(getApplicationContext()).getAlarmRunning()){
                    new sendSMS(Home_Page_Drawer.this)
                            .sendTestMsg(getString(R.string.safeDiaMsg),getString(R.string.safeMsg));
                }
                new alarmManager(getApplicationContext()).alarmCancel();
                safe_btn.setVisibility(View.INVISIBLE);
                buzzer_btn.setVisibility(View.VISIBLE);
                panic_btn.setVisibility(View.VISIBLE);
                if (record_audio.mediaPlayer() != null){
                    record_audio.buzzer_Stop();
                }

            }
        });

    }

    public void StatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setTintColor(ContextCompat.getColor(this, R.color.titlebar));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(getResources().getColor(R.color.titlebar));
        }
    }

    private void setFullVolume(){
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
    }

    public boolean checkConnection(){
        boolean isConnected = false;
        ConnectivityManager manager = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork){
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                isConnected = true;
                Snackbar.make(getWindow().getDecorView(),"connected to mobile network", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                isConnected = true;
                Snackbar.make(getWindow().getDecorView(),"connected to wifi network",BaseTransientBottomBar.LENGTH_SHORT).show();
            }
            panic_btn.setVisibility(View.VISIBLE);
            buzzer_btn.setVisibility(View.VISIBLE);
        }
        else {
            panic_btn.setVisibility(View.INVISIBLE);
            buzzer_btn.setVisibility(View.INVISIBLE);
            connectivityPopup();
            Snackbar.make(getWindow().getDecorView(), "No Internet connection", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
        return isConnected;
    }
    public void connectivityPopup(){
        final MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Home_Page_Drawer.this);
        dialogBuilder.setTitle("No Internet Connection")
                .setMessage("please connect to the Internet to use this app")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkConnection();
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        checkConnection();
        if (new alarmRunningPref(getApplicationContext()).getAlarmRunning()){
            safe_btn.setVisibility(View.VISIBLE);
            panic_btn.setVisibility(View.INVISIBLE);
        }
        else
            safe_btn.setVisibility(View.INVISIBLE);
        if (Record_Audio.mediaPlayer() != null){
            safe_btn.setVisibility(View.VISIBLE);
            buzzer_btn.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), "Tap again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
