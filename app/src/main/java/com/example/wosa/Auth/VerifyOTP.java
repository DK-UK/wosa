package com.example.wosa.Auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wosa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends Fragment {
    public static final String TAG = "Main";

    private String number;
    private FirebaseAuth auth;
    public VerifyOTP(String number, Context context) {
        auth = FirebaseAuth.getInstance();
        Log.e(TAG, "verifyNumber: "+number);
        this.number = number;
        this.context = context;
    }

    private EditText editText1,editText2,editText3,editText4,editText5,editText6;
    private TextView textViewResendCode;
    private Button btnVerifyOTP;
    private String codeSent;
    private UserLoginPref userLoginPref;
    private Context context;
    private Activity activity;
    private RelativeLayout relativeProgress;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_o_t_p, container, false);
        userLoginPref = new UserLoginPref(context);
        textViewResendCode = view.findViewById(R.id.textviewResendCode);

        sendOTP(number);
        
        editText1 = view.findViewById(R.id.editOtp1);
        editText2 = view.findViewById(R.id.editOtp2);
        editText3 = view.findViewById(R.id.editOtp3);
        editText4 = view.findViewById(R.id.editOtp4);
        editText5 = view.findViewById(R.id.editOtp5);
        editText6 = view.findViewById(R.id.editOtp6);
        btnVerifyOTP = view.findViewById(R.id.btnVerifyOTP);
//        relativeProgress = view.findViewById(R.id.relativeLayoutProgressBar);

//        editText1.addTextChangedListener(VerifyOTP.this);
        editText1.addTextChangedListener(new GenericTextWatcher(editText1));
        editText2.addTextChangedListener(new GenericTextWatcher(editText2));
        editText3.addTextChangedListener(new GenericTextWatcher(editText3));
        editText4.addTextChangedListener(new GenericTextWatcher(editText4));
        editText5.addTextChangedListener(new GenericTextWatcher(editText5));
        editText6.addTextChangedListener(new GenericTextWatcher(editText6));


        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBarShow();
                String otp = editText1.getText().toString()
                            + editText2.getText().toString()
                            + editText3.getText().toString()
                            + editText4.getText().toString()
                            + editText5.getText().toString()
                            + editText6.getText().toString();

                verifyOTP(otp);
            }
        });

        textViewResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(number);
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLoginRegistration,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    SignInWithPhone(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeSent = s;
                    Log.e(TAG, "onCodeSent: otp : " + codeSent);
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                }
            };


    private void sendOTP(String num){
        Log.e(TAG, "verifyNumber: "+num);
        Log.e(TAG, "sendOTP: "+context);
        setTimer();
        if (!num.isEmpty()) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    num,
                    60L,
                    TimeUnit.SECONDS,
                    (Activity)context,
                    mCallback
            );
        }
    }

    private void setTimer(){
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                textViewResendCode.setText("00:"+millisUntilFinished / 1000);
                textViewResendCode.setEnabled(false);
                textViewResendCode.setTextColor(getResources().getColor(R.color.grey));
            }

            @Override
            public void onFinish() {
                textViewResendCode.setText("Resend code");
                textViewResendCode.setTextColor(getResources().getColor(R.color.blue));
                textViewResendCode.setEnabled(true);
            }
        }.start();
    }

    private void verifyOTP(String otp){
        if (!otp.isEmpty() && otp.length() == 6){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,otp);
            SignInWithPhone(credential);
        }
        else{
            editText6.requestFocus();
            editText6.setError("Invalid");
        }
    }

    private void SignInWithPhone(PhoneAuthCredential credential) {
//        final Bundle bundle = new Bundle();
//        if (getArguments().getString("fromWhere") != null){
//            String s = getArguments().getString("fromWhere");
//            Log.e(TAG, "SignInWithPhone: "+s);
//            bundle.putString("fromWhere",s);
//        }
        if (!number.isEmpty()) {
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
//                                progressBarHide();
                                userLoginPref.setPhone(number);
                                Register register = new Register(number);
//                                register.setArguments(bundle);
                                replaceFragment(register);
                        }
                            else{
//                                progressBarHide();
                                Log.e(TAG, "onComplete: failed to register");
                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    progressBarHide();
                    Log.e(TAG, "onFailure: "+e.toString());
                }
            });
            }
    }

    class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view){
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable s) {
            String edit = s.toString();

            switch (view.getId()){
                case R.id.editOtp1:
                    if (editText1.getText().toString().length() == 1)
                        editText2.requestFocus();
                    break;
                case R.id.editOtp2:
                    if (editText2.getText().toString().length() == 1){
                        editText3.requestFocus();
                    }
                    else if(editText2.getText().toString().length() == 0)
                        editText1.requestFocus();
                    break;
                case R.id.editOtp3:
                    if (editText3.getText().toString().length() == 1)
                        editText4.requestFocus();
                    else if (editText3.getText().toString().length() == 0)
                        editText2.requestFocus();
                    break;
                case R.id.editOtp4:
                    if (editText4.getText().toString().length() == 1)
                        editText5.requestFocus();
                    else if (editText4.getText().toString().length() == 0)
                        editText3.requestFocus();
                    break;
                case R.id.editOtp5:
                    if (editText5.getText().toString().length() == 1)
                        editText6.requestFocus();
                    else if (editText5.getText().toString().length() == 0)
                        editText4.requestFocus();
                    break;
                case R.id.editOtp6:
                    if (editText6.getText().toString().length() == 0)
                        editText5.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

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