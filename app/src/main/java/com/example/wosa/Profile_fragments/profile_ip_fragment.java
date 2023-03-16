package com.example.wosa.Profile_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wosa.Auth.UserLoginPref;
import com.example.wosa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class profile_ip_fragment extends Fragment {
    public Context context;

    public String TAG = "Main";


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu_save,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    EditText user_fullname, user_age, user_address;
    TextView Phone_num;
    public String Name = "WOSA", Age, Address;
    UserLoginPref regPref;

    public profile_ip_fragment() {
        // Required empty public constructor
    }

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ProgressDialog dialog;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveMenuIcon) {
            saveData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {

        dialog.show();
        Name = user_fullname.getText().toString().trim();
        Age = user_age.getText().toString().trim();
        Address = user_address.getText().toString().trim();


        if ((!TextUtils.isEmpty(Name)) && (!TextUtils.isEmpty(Age)) && (!TextUtils.isEmpty(Address))) {
            try {
                profile_ipClass.setName(Name);
                profile_ipClass.setAge(Age);
                profile_ipClass.setAddress(Address);
                dialog.dismiss();
                save_to_DB();
                replace_fragment(POF);


            } catch (Exception e) {
                dialog.dismiss();
                Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            dialog.dismiss();
            if (TextUtils.isEmpty(Name)) {
                user_fullname.requestFocus();
                user_fullname.setError("Enter valid name");
            }
            else if (TextUtils.isEmpty(Age)){
                user_age.requestFocus();
                user_age.setError("Enter valid age");
            }
            else{
                user_address.requestFocus();
                user_address.setError("Enter valid address");
            }
//                    Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show();

        }

    }

    Toolbar toolbar;
    profile_op_fragment POF;
    profile_ipClass profile_ipClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile_ip_fragment, container, false);
        setHasOptionsMenu(true);

        POF = new profile_op_fragment();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Saving...");

        Phone_num = view.findViewById(R.id.Phone_num);

        profile_ipClass = new profile_ipClass(context);
        regPref = new UserLoginPref(context);

        user_fullname = view.findViewById(R.id.user_fullname);
        user_age = view.findViewById(R.id.user_age);
        user_address = view.findViewById(R.id.user_address);


        if (profile_ipClass.getName() != "") {
            user_fullname.setText(profile_ipClass.getName());
            user_age.setText(profile_ipClass.getAge());
            user_address.setText(profile_ipClass.getAddress());
        }

        Phone_num.setText(regPref.getPhone());
//        save_btn =view.findViewById(R.id.save_btn);

/*
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Name = user_fullname.getText().toString().trim();
                Age = user_age.getText().toString().trim();
                Address = user_address.getText().toString().trim();

                // if the Image is null then we fetch the path from sharedPreference
//                    if (encoded == "" || encoded == null) {
//                        encoded = profile_ipClass.getImgBitmap();
//                        Toast.makeText(context, " entered in PicturePath : " + encoded, Toast.LENGTH_LONG).show();
//                    }

                if ((!TextUtils.isEmpty(Name)) && (!TextUtils.isEmpty(Age)) && (!TextUtils.isEmpty(Address))) {
                    try {
//                            if (encoded != "" && encoded != null) {
//                        WriteData(Name);
                        profile_ipClass.setName(Name);
                        profile_ipClass.setAge(Age);
                        profile_ipClass.setAddress(Address);
                        dialog.dismiss();
                        save_to_DB();
                        replace_fragment(POF);


                    } catch (Exception e) {
                        dialog.dismiss();
                        Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(Name)) {
                        user_fullname.requestFocus();
                        user_fullname.setError("Enter valid name");
                    }
                    else if (!TextUtils.isEmpty(Age)){
                        user_age.requestFocus();
                        user_age.setError("Enter valid age");
                    }
                    else{
                        user_address.requestFocus();
                        user_address.setError("Enter valid address");
                    }
//                    Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show();

                }
            }
        });
*/

//            try {
//                if (profile_ipClass.getImgBitmap() != "") {
//                    byte[] b = Base64.decode(profile_ipClass.getImgBitmap().getBytes(),0);
//                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
//                } else {
//                    Toast.makeText(context, "path null", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                Toast.makeText(context, "EXception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }


        return view;


    }

    public void replace_fragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.replace(R.id.profile_fragment, fragment);
        ft.commit();
    }

    public void save_to_DB() {
//        profile_ipClass profile_input = new profile_ipClass(Name,Age,Address,picturePath);
        Map<String, Object> profile_input = new HashMap<>();
        profile_input.put("Name", Name);
        profile_input.put("Number", regPref.getPhone());
        profile_input.put("Age", Age);
        profile_input.put("Address", Address);
//        profile_input.put("ImagePath",encoded);
        db.collection("Phone")
                .document(regPref.getPhone())
                .collection("UserDetails")
                .document("Profile_info")
                .set(profile_input)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "data added", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "profile data added");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Exce : " + e.getMessage());
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
//        new profile_ipClass(context).clearProfile();
    }
}



//
//    public void callPermission(){
//        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        String rationale = "Permission is required to pick image";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//
//        Permissions.check(context/*context*/, permissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
//                // do your task.
//                Intent i = new Intent(
//                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        i.putExtra("crop","true");
//                        i.putExtra("aspectX",1);
//                        i.putExtra("aspectY",1);
//                        i.putExtra("outputX",200);
//                        i.putExtra("outputY",200);
//                        i.putExtra("return-data",true);
//                        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
//                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                        }
//                    startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                callPermission();
//            }
//        });
//
//
//    }
//

    //public void retrieveFromDB(){
//    db.collection("Phone")
//            .document(regPref.getPhone())
//            .collection(Name)
//            .document("Profile_Info")
//            .get()
//            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    if (documentSnapshot.exists()){
//                        String Name = documentSnapshot.getString("Name");
//                        String Age = documentSnapshot.getString("Age");
//                        String Address = documentSnapshot.getString("Address");
//                        profile_ipClass profile_ipClass = new profile_ipClass(context);
//                        profile_ipClass.setName(Name);
//                        profile_ipClass.setAge(Age);
//                        profile_ipClass.setAddress(Address);
//                    }
//                    else{
//
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception e) {
//            Toast.makeText(context,"Exception : "+e.getMessage(),Toast.LENGTH_SHORT).show();
//        }
//    });
//}
//    public void WriteData(String Name) {
//
//        File path = new File(getActivity().getFilesDir(),"myFolder");
//        Log.e(TAG,"Path : "+path.toString());
//        if (!path.exists()){
//            path.mkdirs();
//            Log.e(TAG, "WriteData:path created");
//        }
//        File mypath = new File(path,"profile.txt");
//        if (mypath.exists())
//            Log.e(TAG, "file existed thus deleted");
//            mypath.delete();
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(mypath));
//            bw.write(Name);
//            Log.e(TAG, "successfully written");
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

