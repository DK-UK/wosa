package com.example.wosa.Home;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.example.wosa.Auth.UserLoginPref;
import com.example.wosa.Buzzer_tone;
import com.example.wosa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;


public class Record_Audio {
    Context context;
    Random random = new Random();
    public static boolean workFinished = false;
    public String TAG = "Main";
    public static MediaPlayer mediaPlayer;
    public static MediaRecorder mediaRecorder;
    public String audioSavePathInDevice = null;
    public String randomFileName = "abcdefghijklmno";
    StorageReference storageRef;

    public Record_Audio(Context context) {
        this.context = context;
    }



    int tone_path;




    public void recordAudio() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO) ==
                PermissionChecker.PERMISSION_GRANTED){
            audioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+/*
                    randomFileName(4)+*/"audioRecording1WOSA.mp4";

            // Set up the values for the new audio file
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.DISPLAY_NAME, "my_audio.mp4");
            values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4");
            values.put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC + "/my_app_audio");

// Get the Uri for the MediaStore Audio collection
            Uri collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_INTERNAL);

// Insert the new audio file into the MediaStore Audio collection
            Uri audioUri = context.getContentResolver().insert(collection, values);

            try {

                context.getContentResolver().openOutputStream(audioUri, "w");

                // Get an OutputStream to the audio file
//                OutputStream outputStream = context.getContentResolver().openOutputStream(audioUri);

                // Write the audio data to the OutputStream
               /* InputStream inputStream = *//* your audio data *//*;
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Close the streams
                inputStream.close();
                outputStream.close();*/
            } catch (IOException e) {
                // Handle the exception
            }


            Log.e(TAG, "recordAudio: path " + audioSavePathInDevice);

            mediaRecorderReady();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Log.e(TAG, "recordAudio");
            } catch (Exception e) {
                Toast.makeText(context,"Exception mediaRecorder : "+e.getMessage(),Toast.LENGTH_LONG).show();
                Log.e(TAG, "recordAudio: exception : " + e.toString());
            }
//            Toast.makeText(context,"audio recording...",Toast.LENGTH_SHORT).show();
        }
        else{
            new userLocation(context).userPermissions(false);
        }
    }


    public void stopAudio(){
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                alarmManager.recordAudioFirstTime = false;
                mediaRecorder = null;
                Log.e(TAG, "stopAudio");
            }catch (Exception e){
                Toast.makeText(context,"exce "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        workFinished = true;
        Log.e(TAG, "workFinished"+workFinished);
        Toast.makeText(context,"recording stopped",Toast.LENGTH_SHORT).show();
    }

    public static boolean getWorkFinished(){
        return workFinished;
    }

//    public void playLastRecoredAudio(){
//        Toast.makeText(context,"recording playing",Toast.LENGTH_SHORT).show();
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(audioSavePathInDevice);
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaPlayer.start();
//    }
//    public void stopPlayingRecording(){
//        if (mediaPlayer != null){
////            Toast.makeText(context,"recording released",Toast.LENGTH_SHORT).show();
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaRecorderReady();
//        }
//    }





    public void startAudioTimer(){
        recordAudio();
        new CountDownTimer(40000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                if (mediaRecorder != null){
                   stopAudio();
                   final recordAudioPref audioPref = new recordAudioPref(context);
                    storageRef = FirebaseStorage.getInstance().getReference();
                    Uri file = Uri.fromFile(new File(audioSavePathInDevice));
                    final StorageReference audioRef = storageRef.child("Audio"+"/"+new UserLoginPref(context).getPhone()+"/");
                    audioRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           audioRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                               @Override
                               public void onComplete(@NonNull Task<Uri> task) {
                                String url = task.getResult().toString();
                                    audioPref.setAudioUrl("");
                                    audioPref.setAudioUrl(url);
                                    audioPref.setOnlyFor1stTime(true);
                                    Log.e(TAG, "onComplete: "+url);
                               }
                           })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Log.e(TAG, "Exception : "+e.getMessage());
                                       }
                                   });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context,"Exception : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onFailure:"+e.getMessage());
                                }
                            });
        }
            }
        }.start();
    }



    
    public void mediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(192000);
        mediaRecorder.setOutputFile(audioSavePathInDevice);

        Log.e(TAG, "mediaRecorderReady: path : " + audioSavePathInDevice);
    }

    public String randomFileName(int string){
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string ){
            stringBuilder.append(randomFileName.charAt(random.nextInt(randomFileName.length())));
            i++;
        }
        return stringBuilder.toString();
    }




    //Buzzer Tones

    public void buzzer_Play(boolean looping){

        tone_path = chooseTone();
        Log.e(TAG, "buzzer_Play:"+tone_path);
        mediaPlayer = null;
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context,tone_path);
            if (looping) {
                mediaPlayer.setLooping(true);
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    mediaPlayer.setLooping(true);
                }
            });
        }
        mediaPlayer.start();
    }

    public void buzzer_Stop(){
        stopPlayer();
    }
    public void stopPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(context,"Media player released",Toast.LENGTH_SHORT).show();
        }
    }

    public int chooseTone(){
        Buzzer_tone buzzer_tone = new Buzzer_tone(context);
        tone_path = R.raw.police;
        String buzzer_value = new Buzzer_tone(context).getBuzzerTone();
        if (buzzer_tone.getBuzzerTone() == "" || buzzer_tone.getBuzzerTone() == null) {
            buzzer_value = "Police Siren1";
        }
        else {
            if (buzzer_value.equalsIgnoreCase("Help 1")){
                tone_path = R.raw.help1;
                Log.e(TAG, "chooseTone: Help 1");
            }
            else if (buzzer_value.equalsIgnoreCase("Help 2")){
                tone_path = R.raw.help3;
                Log.e(TAG, "chooseTone: Help 2");
            }
            if (buzzer_value.equalsIgnoreCase("Female Scream1")) {
                tone_path = R.raw.female;
                Log.e(TAG, "chooseTone female");
            }
            else if(buzzer_value.equalsIgnoreCase("Female Scream2")) {
                tone_path = R.raw.female_scream2;
                Log.e(TAG, "chooseTone: female scream2");
            }else if (buzzer_value.equalsIgnoreCase("Police Siren1")) {
                tone_path = R.raw.police;
                Log.e(TAG, "chooseTone:  Police siren1");
            }
            else if(buzzer_value.equalsIgnoreCase("Police Siren2")){
                tone_path = R.raw.police_siren2;
                Log.e(TAG, "chooseTone: police siren2");
            }
        }
        return tone_path;
    }

    public static MediaRecorder mediaRecorder(){
        return mediaRecorder;
    }
    public static MediaPlayer mediaPlayer(){
        return mediaPlayer;
    }

}
