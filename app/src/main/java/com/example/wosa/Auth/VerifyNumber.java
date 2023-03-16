package com.example.wosa.Auth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wosa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

/**

 */
public class VerifyNumber extends Fragment {


    public VerifyNumber() {
        // Required empty public constructor
    }

    public static final String TAG = "Main";
    private EditText editTextNum,editCountryCode;
//    private Button btnVerifyNum;
//    public Boolean OTP_sent = false;
    private FirebaseFirestore db;
    private Context context;
    private RelativeLayout relativeProgress;
    private CountryCodePicker countryCodePicker;
    private UserLoginPref userLoginPref;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_number, container, false);
        db = FirebaseFirestore.getInstance();
//        relativeProgress = view.findViewById(R.id.relativeLayoutProgressBar);

        userLoginPref = new UserLoginPref(context);

        editTextNum = view.findViewById(R.id.editMobNum);

        countryCodePicker = view.findViewById(R.id.countryCodePicker);

        view.findViewById(R.id.btnVerifyNum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String num = editTextNum.getText().toString().trim();
                String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
                String number = countryCode + num;

                if(!num.isEmpty() && num.length() == 10){
//                    progressBarShow();
                    verifyNumber(number);
                }
                else{
                    editTextNum.requestFocus();
                    editTextNum.setError("Invalid Number");
                }
            }
        });
        return view;
    }

    private void verifyNumber(final String num){
        Log.e(TAG, "verifyNumber: "+num);
        db.collection("Phone")
                .document(num)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String number = documentSnapshot.getString("Phone");
                            if (!number.isEmpty()){
                                userLoginPref.setCountryCode(countryCodePicker.getSelectedCountryCodeWithPlus());
                                Log.e(TAG, "verifyNumber: "+num);
                                Login login = new Login(num);
                                replaceFragment(login);
//                                progressBarHide();
                            }
                        }
                        else{
//                            progressBarHide();
                            Log.e(TAG, "verifyNumber: "+num);
                            userLoginPref.setCountryCode(countryCodePicker.getSelectedCountryCodeWithPlus());
                            VerifyOTP verifyOTP = new VerifyOTP(num,context);
                            replaceFragment(verifyOTP);
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
        ft.addToBackStack(null);
        ft.commit();
    }

    private void progressBarShow(){
        relativeProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void progressBarHide(){
        relativeProgress.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}