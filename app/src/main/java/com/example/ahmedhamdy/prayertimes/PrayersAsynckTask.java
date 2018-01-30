package com.example.ahmedhamdy.prayertimes;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ahmed hamdy on 1/20/2018.
 */

public class PrayersAsynckTask extends AsyncTask<JSONArray,ProgressBar,ArrayList<Prayers>> {

public interface PrayersListListener{
    void prayersListLoaded(ArrayList<Prayers> prayersArrayList);
}
public static PrayersListListener prayersListener;


    @Override
    protected ArrayList<Prayers> doInBackground(JSONArray... jsonArrays) {
       return PrayersHelper.getPrayersAsString(jsonArrays[0]);

    }

    @Override
    protected void onPostExecute(ArrayList<Prayers> prayers) {

        prayersListener.prayersListLoaded(prayers);

    }
}
