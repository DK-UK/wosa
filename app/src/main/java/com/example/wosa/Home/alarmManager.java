package com.example.wosa.Home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class alarmManager {
    private Context context;


    public static boolean recordAudioFirstTime = false;

    public static final String TAG = "Main";

    public alarmManager(Context context){
        this.context = context;
    }

    public void alarmStart(boolean firstTimeForRecord) {

        //to set for alarm is currently running
        new alarmRunningPref(context).setAlarmRunning(true);

        long time;
        if (firstTimeForRecord) {
            Log.e(TAG, "1 Seconds");
            recordAudioFirstTime = true;
            Log.e(TAG, "alarmStart:record audio first time TRUE");
            time = GregorianCalendar.getInstance().getTimeInMillis() + 1000;
        }else {
            Log.e(TAG, "1 minute");
            Log.e(TAG, "alarmStart:record audio first time FALSE");
            time = GregorianCalendar.getInstance().getTimeInMillis() + (1000 * 60 * 1);
        }
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, broadCastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }


        public void alarmCancel(){
        if (new alarmRunningPref(context).getAlarmRunning()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, broadCastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "service stopped", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "alarmCancel");

            new alarmRunningPref(context).setAlarmRunning(false);

            if (userLocation.fusedlocation() != null) {
                userLocation.fusedlocation().removeLocationUpdates(userLocation.locationcallback());
            }

            if (Record_Audio.mediaRecorder() != null) {
                new Record_Audio(context).stopAudio();
            }
        }
        }

        public static boolean getRecordAudioFirstTime(){
            return recordAudioFirstTime;
        }
}
