package com.example.ahmedhamdy.prayertimes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by ahmed hamdy on 1/21/2018.
 */

public class ViewPagerFragment extends Fragment implements LocationHelper.LocationLoadedListener,PrayersAsynckTask.PrayersListListener {

    private RequestQueue mRequest;
    private ArrayList<Prayers> currentMonthPrayersList;
    private ViewPager mPager;
    private MyPagerAdapter myPagerAdapter;
    private ProgressBar pb;
    public static final String PRAYERS_ARRAY_KEY = "prayersArrayList";
    public static final String USER_LAT = "latitude";
    public static final String USER_LONG = "longitude";
    public static final String USER_ALT="altitude";

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState ==null) {
            LocationHelper.mListener = this;
            PrayersAsynckTask.prayersListener = this;
            LocationHelper.getUserLocation(getContext());
        }
        else {


            ValueEventListener  dataListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                         currentMonthPrayersList = getPrayerObjectFromFireBase(dataSnapshot);
                        // Toast.makeText(getContext(),dataSnapshot.child("0").child("fajrTime").getValue().toString(),Toast.LENGTH_LONG).show();
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
           mDatabase.getReference().addValueEventListener(dataListener);

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_day_view_pager, container, false);
        mPager = mainView.findViewById(R.id.pager);
        pb = getActivity().findViewById(R.id.progressbar);
        return mainView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            currentMonthPrayersList = Parcels.unwrap(savedInstanceState.getParcelable(PRAYERS_ARRAY_KEY));
            myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
            mPager.setAdapter(myPagerAdapter);
            pb.setVisibility(View.GONE);
        }



    }

    @Override
    public void isLocationIsLoaded(Location loc) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = mSettings.edit();
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        double altitude = loc.getAltitude();

        FirebaseCrash.log(" User Location has been Loaded");

        // save user location in shared preferences
        editor.putString(USER_LAT,String.valueOf(latitude));
        editor.putString(USER_LONG,String.valueOf(longitude));
        editor.putString(USER_ALT,String.valueOf(altitude));

        final String url = PrayersHelper.PrayerUrl(latitude,longitude
                ,DateHelper.getCurrentMonthAsInt()
                ,DateHelper.getCurrentYear());


        PrayersHelper.setContext(getContext());
        mRequest = Volley.newRequestQueue(getContext());

        if (!isNetworkAvailable())
        {

            Snackbar.make(getView(), R.string.snackbartext,Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry_snackbar
                    , new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isNetworkAvailable())
                        PrayersHelper.getPrayersJsonArray(url, mRequest);
                }
            }).setActionTextColor(Color.WHITE).show();

        }

        else {
            PrayersHelper.getPrayersJsonArray(url, mRequest);
        }
    }

    @Override
    public void prayersListLoaded(ArrayList<Prayers> prayersArrayList) {


        // handle database


        DatabaseReference mRef = mDatabase.getReference();

        mRef.setValue(prayersArrayList);
        FirebaseCrash.log("Prayers Times have been loaded successfully");

        pb.setVisibility(View.GONE);
        currentMonthPrayersList = new ArrayList<>();
        currentMonthPrayersList = prayersArrayList;
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(myPagerAdapter);

    }


    public  class MyPagerAdapter extends FragmentPagerAdapter{

        int MAX_NUM= DateHelper.getMaxNumOfDayesInMonth() - DateHelper.getCurrentDayAsInt()+1;
        // we add 1 to make sure we get all days in fragment
        // for example : if today is 30/1 -----> MAX_NUM =  31 -30 = 1 so we must +1  to get 2 fragments


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // -1 and +1 to handle current day as array begins from index 0 so
            // index 0 equal day 1 and so on
            int index = position + DateHelper.getCurrentDayAsInt()-1;
           // Toast.makeText(getContext(),String.valueOf(currentMonthPrayersList.size()),Toast.LENGTH_LONG).show();
            return PrayersDayFragment.newInstance(currentMonthPrayersList.get(index),index+1);
        }

        @Override
        public int getCount() {
            return MAX_NUM;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(PRAYERS_ARRAY_KEY, Parcels.wrap(currentMonthPrayersList));

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private ArrayList<Prayers> getPrayerObjectFromFireBase(DataSnapshot snapshot){
        ArrayList<Prayers> prayersList  = new ArrayList<>();
        //Prayers temp = new Prayers();
        for (int i=0;i<snapshot.getChildrenCount();i++){
            Prayers temp = new Prayers();
            DataSnapshot dataSnapshot =  snapshot.child(String.valueOf(i));
            temp.setFajrTime(dataSnapshot.child("fajrTime").getValue().toString());
            temp.setSunRiseTime(dataSnapshot.child("sunRiseTime").getValue().toString());
            temp.setDuhrTime(dataSnapshot.child("duhrTime").getValue().toString());
            temp.setAsrTime(dataSnapshot.child("asrTime").getValue().toString());
            temp.setMaghribTime(dataSnapshot.child("maghribTime").getValue().toString());
            temp.setAishaTime(dataSnapshot.child("aishaTime").getValue().toString());
            prayersList.add(temp);

        }

        return prayersList;



    }

}
