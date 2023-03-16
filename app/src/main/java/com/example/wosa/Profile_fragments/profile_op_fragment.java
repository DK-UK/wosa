package com.example.wosa.Profile_fragments;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wosa.Auth.UserLoginPref;
import com.example.wosa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class profile_op_fragment extends Fragment {

    TextView op_name,op_age,op_address,Phone_num;
    String name;
    public profile_op_fragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_op_fragment, container, false);
        setHasOptionsMenu(true);


        profile_ipClass profile_ipClass = new profile_ipClass(getActivity());

        op_name = view.findViewById(R.id.username);
        Phone_num = view.findViewById(R.id.Phone_num);
        op_age = view.findViewById(R.id.age);
        op_address = view.findViewById(R.id.address);


//        Edit = view.findViewById(R.id.edit_btn);
        try{
            if (profile_ipClass.getName() != "") {
                op_name.setText(profile_ipClass.getName());
                Phone_num.setText(new UserLoginPref(getActivity()).getPhone());
                op_age.setText(profile_ipClass.getAge());
                op_address.setText(profile_ipClass.getAddress());
            }

        }catch(Exception e){
            Toast.makeText(getActivity(), "Ex : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editMenuIcon) {
            try {
                final profile_ip_fragment PIF = new profile_ip_fragment();
                replace_fragment(PIF);
            } catch (NullPointerException e) {
                Toast.makeText(getActivity().getApplicationContext(), " getTargetFragment = " + getTargetFragment() +
                        " RequestCode = " + getTargetRequestCode() + " Result_ok = " + Activity.RESULT_OK + " name = " + name + " e = " + e, Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void replace_fragment(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.replace(R.id.profile_fragment,fragment);
        ft.commit();
    }
}
