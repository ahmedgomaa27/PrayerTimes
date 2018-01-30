package com.example.ahmedhamdy.prayertimes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by ahmed hamdy on 1/21/2018.
 */

public class PrayersDayFragment extends Fragment {

    public static final String PRAYERS_DAY_KEY = "prayers";
    public static final String DAY_NUMBER_KEY = "day";
    private  Prayers currentPrayers;
    private int dayDate;
    private String dayDateString=" ";
    private ArrayList<String> prayersNames = new ArrayList<>();
    private TextView mFajrTimeTextView;
    private TextView mSunriseTimeTextView;
    private TextView mZuhrTimeTextView;
    private TextView mAsrTimeTextView;
    private TextView mMaghribTimeTextView;
    private TextView mIshaTimeTextView;

    public static PrayersDayFragment newInstance(Prayers dayPrayers , int dayNumber){
        Bundle args = new Bundle();
        args.putParcelable(PRAYERS_DAY_KEY, Parcels.wrap(dayPrayers));

        args.putInt(DAY_NUMBER_KEY,dayNumber);
        PrayersDayFragment fragment = new PrayersDayFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPrayers = Parcels.unwrap(getArguments().getParcelable(PRAYERS_DAY_KEY));

        dayDate = getArguments().getInt(DAY_NUMBER_KEY);
        if (dayDate == DateHelper.getCurrentDayAsInt())
        {
            dayDateString = "Today ";
        }
        dayDateString = dayDateString + String.valueOf(dayDate) + " " + DateHelper.getCurrentMonthAsString()
                + " " + String.valueOf(DateHelper.getCurrentYear());
        prayersNames.add(getString(R.string.Fajr));
        prayersNames.add(getString(R.string.Sunrise));
        prayersNames.add(getString(R.string.Dhuhr));
        prayersNames.add(getString(R.string.Asr));
        prayersNames.add(getString(R.string.Maghrib));
        prayersNames.add(getString(R.string.Isha));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_day_view,container,false);


        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView dayDateView = view.findViewById(R.id.tv_day_date);
        View fajrCard = view.findViewById(R.id.fajr_card);
        View sunriseCard = view.findViewById(R.id.sunrise_card);
        View zuhrCard = view.findViewById(R.id.zuhr_card);
        View asrCard = view.findViewById(R.id.asr_card);
        View maghribCard = view.findViewById(R.id.maghrib_card);
        View ishaCard = view.findViewById(R.id.isha_card);

        ((TextView) fajrCard.findViewById(R.id.tv_prayer_name)).setText(prayersNames.get(0));
        ((TextView) sunriseCard.findViewById(R.id.tv_prayer_name)).setText(prayersNames.get(1));
        ((TextView) zuhrCard.findViewById(R.id.tv_prayer_name)).setText(prayersNames.get(2));
        ((TextView) asrCard.findViewById(R.id.tv_prayer_name)).setText(prayersNames.get(3));
        ((TextView) maghribCard.findViewById(R.id.tv_prayer_name)).setText(prayersNames.get(4));
        ((TextView) ishaCard.findViewById(R.id.tv_prayer_name)).setText(prayersNames.get(5));

        mFajrTimeTextView =  fajrCard.findViewById(R.id.tv_prayer_time);
        mSunriseTimeTextView =  sunriseCard.findViewById(R.id.tv_prayer_time);
        mZuhrTimeTextView =  zuhrCard.findViewById(R.id.tv_prayer_time);
        mAsrTimeTextView =  asrCard.findViewById(R.id.tv_prayer_time);
        mMaghribTimeTextView =  maghribCard.findViewById(R.id.tv_prayer_time);
        mIshaTimeTextView =  ishaCard.findViewById(R.id.tv_prayer_time);

        dayDateView.setText(dayDateString);
        mFajrTimeTextView.setText(currentPrayers.getFajrTime());
        mSunriseTimeTextView.setText(currentPrayers.getSunRiseTime());
        mZuhrTimeTextView.setText(currentPrayers.getDuhrTime());
        mAsrTimeTextView.setText(currentPrayers.getAsrTime());
        mMaghribTimeTextView.setText(currentPrayers.getMaghribTime());
        mIshaTimeTextView.setText(currentPrayers.getAishaTime());

    }
}
