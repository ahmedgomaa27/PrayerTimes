package com.example.ahmedhamdy.prayertimes;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ahmed hamdy on 1/20/2018.
 */

public class PrayersAsynckTask extends AsyncTask<JSONArray, ProgressBar, ArrayList<Prayers>> {

    public static PrayersListListener prayersListener;

    @Override
    protected ArrayList<Prayers> doInBackground(JSONArray... jsonArrays) {

        FirebaseCrash.log("Asynck Task to manage prayers starts background operations");

        return PrayersHelper.getPrayersAsString(jsonArrays[0]);


    }

    @Override
    protected void onPostExecute(ArrayList<Prayers> prayers) {

        FirebaseCrash.log("Asynck Task finished background operations");
        prayersListener.prayersListLoaded(prayers);

    }

    public interface PrayersListListener {
        void prayersListLoaded(ArrayList<Prayers> prayersArrayList);
    }
}
