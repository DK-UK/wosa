package com.example.wosa.Profile_fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wosa.Auth.UserLoginPref;
import com.example.wosa.Home.Home_Page_Drawer;
import com.example.wosa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class Profile extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    static String Name = "";

    public boolean registeredUser;
    public String TAG = "Main";
    profile_ipClass profile_ipClass;
     profile_ip_fragment PIF;
     profile_op_fragment POF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To change the Status Bar Color
        StatusBarColor();
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        POF = new profile_op_fragment();
        PIF = new profile_ip_fragment();
        profile_ipClass = new profile_ipClass(Profile.this);

        if (profile_ipClass.getName() != "") {
            Log.e(TAG,"getName found");
            add_fragment(POF);
        }
        else{
            retrieveFromDB();
        }


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home_Page_Drawer.class));
                finish();
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
    public void add_fragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.add(R.id.profile_fragment,fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Home_Page_Drawer.class));
        finish();
    }






    public void retrieveFromDB(){

        profile_ipClass = new profile_ipClass(getApplicationContext());

            db.collection("Phone")
                    .document(new UserLoginPref(getApplicationContext()).getPhone())
                    .collection("UserDetails")
                    .document("Profile_info")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                    String Name = documentSnapshot.getString("Name");
                                    String Age = documentSnapshot.getString("Age");
                                    String Address = documentSnapshot.getString("Address");
                                    Log.e(TAG,"Name : "+Name);

                                        profile_ipClass.setName(Name);
                                        profile_ipClass.setAge(Age);
                                        profile_ipClass.setAddress(Address);
                                        if (!Name.isEmpty()){
                                            add_fragment(POF);
                                        }
                                Log.e(TAG, "Name : "+profile_ipClass.getName()+"\nAge :"
                                +profile_ipClass.getAge()+"\nAddress : "+profile_ipClass.getAddress());
//                                    }

                            }
                            else{
                                Log.e(TAG,"doc not found");

                                add_fragment(PIF);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"failed to get documents"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.e(TAG,e.getMessage());
                            add_fragment(PIF);
                        }
                    });

        Log.e(TAG,"returning Name : "+profile_ipClass.getName());
    }


}
