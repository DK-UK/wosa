package com.example.wosa.Home;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static android.os.Looper.getMainLooper;

public class userLocation {

public static final String TAG = "Main";
   static FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
   static LocationCallback locationCallback;

    Context context;
    String url;
    public userLocation(Context context) {
        this.context = context;
    }

    public void requestLocationUpdates(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED) {
            Log.e(TAG, "requestLocationUpdates called");
            fusedLocationClient = new FusedLocationProviderClient(context);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setFastestInterval(30000);
            locationRequest.setInterval(10000);

            locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    final Double latitude = locationResult.getLastLocation().getLatitude();
                    final Double longitude = locationResult.getLastLocation().getLongitude();
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                    try {
                        String add = getLocationAddress(latitude,longitude);
                        if (add != null){
                            add = "\nLocation : "+add;
                        }
                        else
                            add = "";

//                        Toast.makeText(context,"Location url : "+add,Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Full Address "+add);
                        url = "http://maps.google.com/?q="+latitude+","+longitude+add;
//                        Toast.makeText(context,"URL : "+url,Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "URL : "+url);
                        if (latitude != null && longitude != null) {

                            Log.e(TAG, "onLocationResult: lat : "+latitude+" Long : "+longitude);
                            new sendSMS(context).sendSMS(url);
                            Log.e(TAG, "LocationCallBack called");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Log.e(TAG, "locationcallback "+locationCallback.toString());
//            fusedLocationClient.removeLocationUpdates(locationCallback);
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,getMainLooper());
        }
        else{
            userPermissions(false);
        }
    }

    public String getLocationAddress(Double latitude,Double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude,longitude,1);
        String add = addresses.get(0).getAddressLine(0);
        return add;
    }

    public static FusedLocationProviderClient fusedlocation(){
        return fusedLocationClient;
    }
    public static LocationCallback locationcallback(){
        return locationCallback;
    }

    public void userPermissions(final boolean callFromHome){
        String[] permissions = {Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE
        };
        String rationale = "permission required";
        Permissions.Options options = new Permissions.Options()
                .setCreateNewTask(true)
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");
        Permissions.check(context/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task;
                if (callFromHome){
                    requestLocationUpdates();
//                    new Record_Audio(context).startAudioTimer();
                }
            }
            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
                userPermissions(false);
            }
        });
    }
}
