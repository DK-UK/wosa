package com.example.wosa.Recepients_fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wosa.Auth.UserLoginPref;
import com.example.wosa.Home.sendSMS;
import com.example.wosa.Home.userLocation;
import com.example.wosa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class recepient_ip_fragment extends Fragment implements View.OnClickListener {
    public Context context;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu_save,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveMenuIcon) {
            numConfirmation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    String name,number;
    int btnId;
    public String TAG = "Main";
    public recepient_ip_fragment() {
        // Required empty public constructor
    }
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    phoneDetail phoneDetail;
    TextView num1,num2,num3,num4,num5;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recepient_ip_fragment,container,false);
        setHasOptionsMenu(true);

        new userLocation(context).userPermissions(false);

        phoneDetail = new phoneDetail(context);
//        phoneDetail.clear();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Saving...");

        num1 = view.findViewById(R.id.num1);
        num2 = view.findViewById(R.id.num2);
        num3 = view.findViewById(R.id.num3);
        num4 = view.findViewById(R.id.num4);
        num5 = view.findViewById(R.id.num5);

        b1 = view.findViewById(R.id.btn1);
        b2 = view.findViewById(R.id.btn2);
        b3 = view.findViewById(R.id.btn3);
        b4 = view.findViewById(R.id.btn4);
        b5 = view.findViewById(R.id.btn5);

//        save_btn = view.findViewById(R.id.save_btn);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);

        //save_btn.setOnClickListener(this);

        return view;
    }



    public void ReadContacts(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // BoD con't: CONTENT_TYPE instead of CONTENT_ITEM_TYPE
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, 1);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = context.getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY},
                            null, null, null);

                    if (c != null && c.moveToFirst()) {
                        number = c.getString(0);
                        name = c.getString(1);

                        if (!number.startsWith(new UserLoginPref(context).getCountryCode())){
                            if(number.startsWith("0")) {

                                number = new UserLoginPref(context).getCountryCode() + number.substring(1,11);
                                Log.e(TAG, "onActivityResult: NUMBER : "+ number);
                            }
                        }
                            if(btnId == R.id.btn1){
                                    phoneDetail.setName1(name);
                                    phoneDetail.setNumber1(number);

                            }
                           else if(btnId == R.id.btn2) {
                                phoneDetail.setName2(name);
                                phoneDetail.setNumber2(number);

                            }
                            else if(btnId == R.id.btn3) {
                                phoneDetail.setName3(name);
                                phoneDetail.setNumber3(number);

                            }
                            else if(btnId == R.id.btn4) {
                                phoneDetail.setName4(name);
                                phoneDetail.setNumber4(number);


                            }
                            else if(btnId == R.id.btn5) {
                                phoneDetail.setName5(name);
                                phoneDetail.setNumber5(number);

                            }
                            else{Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();}

//                        showSelectedNumber(number,name);

                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
            case R.id.btn5:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
                btnId = readId(v);
                ReadContacts();
                displayText();
                break;
            default:
                break;
        }
        }
    public int readId(View v){
        return v.getId();
    }
    public void displayText(){
        if(phoneDetail.getName1() != ""){
            num1.setText("Name : "+phoneDetail.getName1()+"\n"+"Number : "+phoneDetail.getNumber1());
        }
        if(phoneDetail.getName2() != ""){
            num2.setText("Name : "+phoneDetail.getName2()+"\n"+"Number : "+phoneDetail.getNumber2());
        }
        if(phoneDetail.getName3() != ""){
            num3.setText("Name : "+phoneDetail.getName3()+"\n"+"Number : "+phoneDetail.getNumber3());
        }
        if(phoneDetail.getName4() != ""){
           num4.setText("Name : "+phoneDetail.getName4()+"\n"+"Number : "+phoneDetail.getNumber4());
        }
        if(phoneDetail.getName5() != ""){
            num5.setText("Name : "+phoneDetail.getName5()+"\n"+"Number : "+phoneDetail.getNumber5());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
//        phoneDetail.clearRecepients();
//        phoneDetail.clearData();
        if (phoneDetail.getName1().isEmpty()){
            retrieveFromDB();
        }else {
            displayText();
        }
    }
    public void save_to_DB() {

            Map<String, Object> recepient_input = new HashMap<>();
            recepient_input.put("Recepient1_name", phoneDetail.getName1());
            recepient_input.put("Recepient1_number", phoneDetail.getNumber1());
            recepient_input.put("Recepient2_name", phoneDetail.getName2());
            recepient_input.put("Recepient2_number", phoneDetail.getNumber2());
            recepient_input.put("Recepient3_name", phoneDetail.getName3());
            recepient_input.put("Recepient3_number", phoneDetail.getNumber3());
            recepient_input.put("Recepient4_name", phoneDetail.getName4());
            recepient_input.put("Recepient4_number", phoneDetail.getNumber4());
            recepient_input.put("Recepient5_name", phoneDetail.getName5());
            recepient_input.put("Recepient5_number", phoneDetail.getNumber5());
            db.collection("Phone")
                    .document(new UserLoginPref(context).getPhone())
                    .collection("UserDetails")
                    .document("Recepients_Info")
                    .set(recepient_input)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(context, "Data added", Toast.LENGTH_SHORT).show();
                                Log.e(TAG,"recepients : data added");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure: "+e.getMessage());
                }
            });



    }

    private void numConfirmation() {
        if (phoneDetail.getNumber1() != "" && phoneDetail.getNumber2() != "" && phoneDetail.getNumber3() != ""
                && phoneDetail.getNumber4() != "" && phoneDetail.getNumber5() != "") {

            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog.setIcon(R.drawable.ic_error_outline_black)
                    .setMessage("Are you sure to register those contacts as your emergency contacts ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save_to_DB();
                            new sendSMS(context).sendTestMsg(getString(R.string.testMsg),getString(R.string.sendMsg));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        else{

            Toast.makeText(context,"please choose all 5 contacts",Toast.LENGTH_SHORT).show();
        }
    }

    public void retrieveFromDB(){
        db.collection("Phone")
                .document(new UserLoginPref(context).getPhone())
                .collection("UserDetails")
                .document("Recepients_Info")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String rec1_name = documentSnapshot.getString("Recepient1_name");
                            String rec1_num = documentSnapshot.getString("Recepient1_number");
                            phoneDetail.setName1(rec1_name);
                            phoneDetail.setNumber1(rec1_num);
                            String rec2_name = documentSnapshot.getString("Recepient2_name");
                            String rec2_num = documentSnapshot.getString("Recepient2_number");
                            phoneDetail.setName2(rec2_name);
                            phoneDetail.setNumber2(rec2_num);
                            String rec3_name = documentSnapshot.getString("Recepient3_name");
                            String rec3_num = documentSnapshot.getString("Recepient3_number");
                            phoneDetail.setName3(rec3_name);
                            phoneDetail.setNumber3(rec3_num);
                            String rec4_name = documentSnapshot.getString("Recepient4_name");
                            String rec4_num = documentSnapshot.getString("Recepient4_number");
                            phoneDetail.setName4(rec4_name);
                            phoneDetail.setNumber4(rec4_num);
                            String rec5_name = documentSnapshot.getString("Recepient5_name");
                            String rec5_num = documentSnapshot.getString("Recepient5_number");
                            phoneDetail.setName5(rec5_name);
                            phoneDetail.setNumber5(rec5_num);

                            displayText();
                        }
                        else{
                            Log.e(TAG, "onSuccess: doc not found");
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

