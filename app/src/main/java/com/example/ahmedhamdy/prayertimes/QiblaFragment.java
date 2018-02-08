package com.example.ahmedhamdy.prayertimes;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by ahmed hamdy on 1/29/2018.
 */

public class QiblaFragment extends android.support.v4.app.Fragment implements SensorEventListener {

    public static final String USER_LAT = "latitude";
    public static final String USER_LONG = "longitude";
    public static final String USER_ALT="altitude";


    public static ImageView compassImage,arrowImage;

    // record the compass picture angle turned
    private float currentDegree = 0f;
    private float currentDegreeNeedle = 0f;
    Context mContext;
    Location userLoc = new Location("service Provider");
    // device sensor manager
    private static SensorManager mSensorManager ;
    private Sensor sensor;

    public QiblaFragment(){


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mSensorManager =  (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if(sensor!=null) {
            // for the system's orientation sensor registered listeners
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);//SensorManager.SENSOR_DELAY_Fastest
        }else{
            Toast.makeText(mContext, R.string.sensor_not_supported, Toast.LENGTH_SHORT).show();
        }



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.qibla_fragment,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compassImage = view.findViewById(R.id.compass_image);
        arrowImage = view.findViewById(R.id.arrow_image);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);
        float head = Math.round(event.values[0]);
        float bearTo;
        Location destinationLoc = new Location("service Provider");

        destinationLoc.setLatitude(21.422487); //kaaba latitude setting
        destinationLoc.setLongitude(39.826206); //kaaba longitude setting
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        double user_Lat = Double.valueOf(mSettings.getString(USER_LAT,"31.11125"));
        userLoc.setLatitude(user_Lat);
        double user_Long = Double.valueOf(mSettings.getString(USER_LONG,"29.3125"));
        userLoc.setLongitude(user_Long);
        double user_alt = Double.valueOf(mSettings.getString(USER_ALT,"21.11125"));
        userLoc.setAltitude(user_alt);




        bearTo=userLoc.bearingTo(destinationLoc);
        //bearTo = The angle from true north to the destination location from the point we're your currently standing.

        //head = The angle that you've rotated your phone from true north.

        GeomagneticField geoField = new GeomagneticField( Double.valueOf( userLoc.getLatitude()).floatValue(), Double
                .valueOf( userLoc.getLongitude() ).floatValue(),
                Double.valueOf( userLoc.getAltitude() ).floatValue(),
                System.currentTimeMillis() );
        head -= geoField.getDeclination(); // converts magnetic north into true north

        if (bearTo < 0) {
            bearTo = bearTo + 360;
            //bearTo = -100 + 360  = 260;
        }

    //This is where we choose to point it
        float direction = bearTo - head;

      // If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction < 0) {
            direction = direction + 360;
        }

        RotateAnimation qiblaAnimation = new RotateAnimation(currentDegreeNeedle, direction, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        qiblaAnimation.setDuration(210);
        qiblaAnimation.setFillAfter(true);

        arrowImage.startAnimation(qiblaAnimation);

        currentDegreeNeedle = direction;

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation compassAnimation = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        // how long the animation will take place
        compassAnimation.setDuration(210);



        // set the animation after the end of the reservation status
        compassAnimation.setFillAfter(true);


        // Start the animation
        compassImage.startAnimation(compassAnimation);

        currentDegree = -degree;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }
}
