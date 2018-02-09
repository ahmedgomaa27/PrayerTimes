package com.example.ahmedhamdy.prayertimes;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * Created by ahmed hamdy on 1/16/2018.
 */

public class LocationHelper {

    private static FusedLocationProviderClient mFusedLocationClient;

    interface LocationLoadedListener {
        void isLocationIsLoaded(Location loc);
    }

    public static LocationLoadedListener mListener;


    public static void getUserLocation(final Activity activity) {


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);


        if (ActivityCompat.checkSelfPermission(activity
                , android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {


                //Toast.makeText(activity.getApplicationContext(), "Sucess ", Toast.LENGTH_LONG).show();
                if (location != null) {

                    mListener.isLocationIsLoaded(location);
                }


            }
        });

    }
}
