package com.example.ahmedhamdy.prayertimes;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState ==null) {
            LocationHelper.mListener = this;
            PrayersAsynckTask.prayersListener = this;
            LocationHelper.getUserLocation(getContext());
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

        // save user location in shared preferences
        editor.putString(USER_LAT,String.valueOf(latitude));
        editor.putString(USER_LONG,String.valueOf(longitude));
        editor.putString(USER_ALT,String.valueOf(altitude));

        String url = PrayersHelper.PrayerUrl(latitude,longitude
                ,DateHelper.getCurrentMonthAsInt()
                ,DateHelper.getCurrentYear());

        PrayersHelper.setContext(getContext());
        mRequest = Volley.newRequestQueue(getContext());
        PrayersHelper.getPrayersJsonArray(url,mRequest);

    }

    @Override
    public void prayersListLoaded(ArrayList<Prayers> prayersArrayList) {

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

}
