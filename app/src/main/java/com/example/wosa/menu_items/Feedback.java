package com.example.wosa.menu_items;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.wosa.Auth.UserLoginPref;
import com.example.wosa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashMap;

public class Feedback extends AppCompatActivity {
    Toolbar toolbar;
    RadioGroup radioGroup;
    private EditText feedback;
    FirebaseFirestore db;
    String TOPIC = "";
    ProgressDialog progressDialog;
    public static String TAG = "Main";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sendMenuIcon){
                progressDialog.show();
                save_to_DB(feedback);
            }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColor();
        setContentView(R.layout.activity_feedback);
        toolbar = findViewById(R.id.include);
        radioGroup = findViewById(R.id.radiobtn_grp);
        feedback = findViewById(R.id.feedback_text);
//        feedback_btn = findViewById(R.id.feedback_sub_btn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");

        db = FirebaseFirestore.getInstance();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("Feedback");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
                finish();
            }
        });

        TOPIC = "suggestion";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.error :
                        TOPIC = "error";
                        Log.e(TAG, "Error clicked");
                        break;
                    case R.id.suggestion:
                        TOPIC = "suggestion";
                        Log.e(TAG, "suggestion clicked");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Value not found..",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void save_to_DB(final EditText edtFeed){
        String feed = feedback.getText().toString().trim();
        if(!feed.isEmpty()) {
            HashMap<String, Object> feedback = new HashMap<>();
            feedback.put("Topic", TOPIC);
            feedback.put("Phone", new UserLoginPref(getApplicationContext()).getPhone());
            feedback.put("feedback", feed);
            db.collection("Feedback")
                    .document(TOPIC)
                    .set(feedback)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                edtFeed.setText("");
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "feedback received", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "sending failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            progressDialog.dismiss();
            feedback.requestFocus();
            feedback.setError("Field is required");
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Settings.class));
        finish();
    }


}
