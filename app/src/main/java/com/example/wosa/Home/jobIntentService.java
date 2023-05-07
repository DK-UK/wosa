package com.example.wosa.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

public class jobIntentService extends JobIntentService {
    public String TAG = "Main";
    public static boolean workFinished = false;
    public boolean recordAudioFirstTime = false;
    public static final int JOB_ID = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context,jobIntentService.class,JOB_ID,intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
//        new Home_Page_Drawer().setAlarm(true,false,getApplicationContext());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e(TAG, "onHandleWork");
        new Thread(new Runnable() {
            @Override
            public void run() {
              Handler handler = new Handler(Looper.getMainLooper());
              handler.postDelayed(new Runnable() {
                  @Override
                  public void run() {

                      new userLocation(getApplicationContext()).userPermissions(true);

                     /* if(alarmManager.getRecordAudioFirstTime()){
                                alarmManager.recordAudioFirstTime = false;
//                      Log.e(TAG, "run: After User Location Audio stat : "+new recordAudioPref(getApplicationContext()).isRecordAudioForFirstTime());
//                       if(new recordAudioPref(getApplicationContext()).isRecordAudioForFirstTime()){
//                          new recordAudioPref(getApplicationContext()).setRecordAudioForFirstTime(false);
//                              new Record_Audio(getApplicationContext()).startAudioTimer();

                              if (Record_Audio.getWorkFinished()) {
                                  Log.e(TAG, "workFinished");
                                  new alarmManager(getApplicationContext()).alarmStart(false);
                              }
                      }
                       else{
                           Log.e(TAG, "audio already recorded");
                       }*/
                  }
              },0);
            }
        }).start();

    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        new alarmManager(getApplicationContext()).alarmStart(false);
        return super.onStartCommand(intent, flags, startId);
    }

}
