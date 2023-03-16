package com.example.wosa.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wosa.Home.Home_Page_Drawer;
import com.example.wosa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;


public class Register extends Fragment {

    public static final String TAG = "Main";
    private String number;
    public Register(String number) {
        this.number = number;
    }

    private EditText editTextPassword,editTextRePassword;
    private FirebaseFirestore db;
    private Context context;
    private UserLoginPref userLoginPref;
    private RelativeLayout relativeProgress;
    private TextView textViewHeader;
    private Button btnRegister;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        userLoginPref = new UserLoginPref(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        db = FirebaseFirestore.getInstance();

            editTextPassword = view.findViewById(R.id.editPassword);
            editTextRePassword = view.findViewById(R.id.editRePassword);
//            relativeProgress = view.findViewById(R.id.relativeLayoutProgressBar);
            textViewHeader = view.findViewById(R.id.textViewHeader);
            btnRegister = view.findViewById(R.id.btnRegister);

//        if (getArguments().getString("fromWhere") != null){
//            textViewHeader.setText("Change Password");
//            btnRegister.setText("Save");
//        }

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pass = editTextPassword.getText().toString().trim();
                    String rePass = editTextRePassword.getText().toString().trim();

                    if (!pass.isEmpty()){
                        if (!rePass.isEmpty()){
                            if (rePass.equals(pass)){
//                                progressBarShow();
                                /** Save User Credential to DB */
                                saveToDB(number,pass);
                            }
                            else {
                                editTextRePassword.setError("password doesn't match");
                                editTextRePassword.requestFocus();
                            }
                        }
                        else
                            editTextRePassword.setError("Invalid");
                    }
                    else{
                        editTextPassword.setError("Invalid");
                    }

                }
            });
        return view;
    }

    private void saveToDB(final String number, String password){
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("Phone",number);
        userInfo.put("Password",password);

        db.collection("Phone")
                .document(number)
                .set(userInfo, SetOptions.mergeFields("Phone","Password"))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            progressBarHide();
                            userLoginPref.setPhone(number);
                            startActivity(new Intent(context, Home_Page_Drawer.class));
                            getActivity().finish();
                            Toast.makeText(context,"successful", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onComplete: register successfully");
                        }
                        else {
//                            progressBarHide();
                            Log.e(TAG, "onComplete: failed to registed");
                            Toast.makeText(context,"failed to register", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        progressBarHide();
                        Log.e(TAG, "onFailure: "+e.toString());
                    }
                });
    }

//    private void progressBarShow(){
//        relativeProgress.setVisibility(View.VISIBLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }

//    private void progressBarHide(){
//        relativeProgress.setVisibility(View.GONE);
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
}