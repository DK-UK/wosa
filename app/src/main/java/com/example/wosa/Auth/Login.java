package com.example.wosa.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wosa.Home.Home_Page_Drawer;
import com.example.wosa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends Fragment {

    public static final String TAG = "Main";
    private String number;
    public Login(String number) {
        this.number = number;
    }

    private EditText editTextPassword;
    private FirebaseFirestore db;
    private Context context;
    private UserLoginPref userLoginPref;
    private RelativeLayout relativeProgress;
    private TextView textViewForgotPass;

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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        db = FirebaseFirestore.getInstance();

            editTextPassword = view.findViewById(R.id.editPassword);
//            relativeProgress = view.findViewById(R.id.relativeLayoutProgressBar);
            textViewForgotPass = view.findViewById(R.id.textViewForgotPass);

            textViewForgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyOTP verifyOTP = new VerifyOTP(number,context);
                    Bundle bundle = new Bundle();
                    bundle.putString("fromWhere","forgotPassword");
                    verifyOTP.setArguments(bundle);
                    replaceFragment(verifyOTP);
                }
            });

            view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password = editTextPassword.getText().toString().trim();

                    if (!password.isEmpty()){
//                        progressBarShow();
                        verifyPassword(password);
                    }
                    else{
                        editTextPassword.requestFocus();
                        editTextPassword.setError("Invalid");
                    }
                }
            });
        return view;
    }

    private void verifyPassword(final String password){
        db.collection("Phone")
                .document(number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String pass = task.getResult().getString("Password");
                            if (password.equals(pass)){
//                                progressBarHide();
                                userLoginPref.setPhone(number);
                                startActivity(new Intent(context, Home_Page_Drawer.class));
                                getActivity().finish();
                            }
                            else{
//                                progressBarHide();
                                editTextPassword.requestFocus();
                                editTextPassword.setError("Invalid password");
                            }
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

    private void replaceFragment(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLoginRegistration,fragment);
        ft.commit();
    }

//    private void progressBarShow(){
//        relativeProgress.setVisibility(View.VISIBLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
//
//    private void progressBarHide(){
//        relativeProgress.setVisibility(View.GONE);
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
}