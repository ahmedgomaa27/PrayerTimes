package com.example.ahmedhamdy.prayertimes;

import android.content.Context;
import android.location.Location;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by ahmed hamdy on 1/16/2018.
 */

public class LocationHelper {

    interface LocationLoadedListener {
        void isLocationIsLoaded(Location loc);
    }
    public static LocationLoadedListener mListener;


    public static void getUserLocation(Context context){

        SmartLocation.with(context).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {


                mListener.isLocationIsLoaded(location);

            }
        });

    }
}
