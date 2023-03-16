package com.example.wosa.Home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.example.wosa.R;
import com.example.wosa.Recepients_fragment.phoneDetail;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class sendSMS {

Context context;
String[] numbers;
public String TAG = "Main";
public String audioUrl = null;
public sendSMS(Context context){
    this.context = context;
}




    public void sendSMS(String url) {

        if (ContextCompat.checkSelfPermission(context,Manifest.permission.SEND_SMS) ==
                PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE) ==
                PermissionChecker.PERMISSION_GRANTED) {
            Log.e(TAG, "sendSMS called");
            SmsManager smsManager = SmsManager.getDefault();
            String Url = url;
            phoneDetail phoneDetail = new phoneDetail(context);
            numbers = new String[]{phoneDetail.getNumber1(),
                    phoneDetail.getNumber2(),
                    phoneDetail.getNumber3(),
                    phoneDetail.getNumber4(),
                    phoneDetail.getNumber5()
            };
            recordAudioPref audioPref = new recordAudioPref(context);
            String message = "help me!!! this is my approximate location : "+url;
            if (audioPref.isOnlyFor1stTime()) {
                Log.e(TAG, "onlyFor1stTime true");
                if (audioPref.getAudioUrl() != "") {
                    message += "\nAudio : "+audioPref.getAudioUrl();
                    Log.e(TAG, "Inner message : "+message);
                    audioPref.setOnlyFor1stTime(false);
                }
            }
            else {
                message = "help me!!! this is my approximate location : "+url;
            }
            ArrayList<String> parts = smsManager.divideMessage(message);
            Log.e(TAG, "sendSMS: Outer Message : "+message);
            Log.e(TAG, "ArrayList parts : "+parts);
            for(String number : numbers){
                    smsManager.sendMultipartTextMessage(number, null,parts, null, null);
                    Log.e(TAG, "sendSMS: "+number+" msg sent");
            }
        }

        else{
            new userLocation(context).userPermissions(false);
        }
    }

    public void sendTestMsg(String dialogMsg, final String sendMsg) {
    final phoneDetail phoneDetail = new phoneDetail(context);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
        final SmsManager smsManager = SmsManager.getDefault();
//        dialog.setIcon(R.drawable.ic_error_outline_black)
        dialog.setIcon(R.drawable.ic_message_black_24dp)
                .setCancelable(false)
                .setTitle("Notify them ?")
                .setMessage(dialogMsg)
                .setCancelable(false)
                .setPositiveButton("Yes,Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //send test messages
                        String numbers[] = {phoneDetail.getNumber1(),
                                phoneDetail.getNumber2(),
                                phoneDetail.getNumber3(),
                                phoneDetail.getNumber4(),
                                phoneDetail.getNumber5()
                        };
                        for(String number : numbers){
                            smsManager.sendTextMessage(number,null,sendMsg+"[WOSA]",null,null);
                        }
                        Toast.makeText(context,"message sent",Toast.LENGTH_SHORT).show();

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


}
