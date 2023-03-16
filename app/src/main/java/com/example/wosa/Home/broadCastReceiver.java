package com.example.wosa.Home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;




public class broadCastReceiver extends BroadcastReceiver {
    public static final String TAG = "Main";
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e(TAG, "onReceive");


        jobIntentService.enqueueWork(context,intent);
    }

}
