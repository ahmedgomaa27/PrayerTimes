package com.example.ahmedhamdy.prayertimes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ahmed hamdy on 1/16/2018.
 */

public class PrayersHelper {

  // public static ArrayList<String> currentMonthTimesArray;
    ArrayList<String> preMonthTimesArray;
    ArrayList<String> nextMonthTimesArray;

    public static final String PRAYER_JSON_ARRAY_KEY = "data";
    public static final String PRAYER_TIMES_KEY = "timings";
    public static final String BASE_URL_PRAYER_TIMES = "http://api.aladhan.com/calendar?";
    public static final String BASE_LAT = "latitude=";
    public static final String BASE_LONG = "&longitude=";
    // here method will be constant for egyptian calculation institute
    public static final String BASE_METHOD = "&method=5";
    public static final String BASE_MONTH = "&month=";
    public static final String BASE_YEAR = "&year=";



    public static final String FAJR = "Fajr";
    public static final String SUN_RISE = "Sunrise";
    public static final String DUHR = "Dhuhr";
    public static final String ASR = "Asr";
    public static final String MAGHRIB = "Maghrib";
    public static final String AISHA = "Isha";

    private static Context mContext;
    public static  String stringJsonArray;

    public static void setContext(Context context){
        mContext = context;
    }
public static String PrayerUrl(double lat,double longt,int monthNum,int yearNum){


        // http://api.aladhan.com/calendar?latitude=51.508515&longitude=-0.1254872&method=2&month=4&year=2017

        return BASE_URL_PRAYER_TIMES + BASE_LAT + String.valueOf(lat)
                + BASE_LONG + String.valueOf(longt)
                + BASE_METHOD +BASE_MONTH
                + String.valueOf(monthNum)
                + BASE_YEAR + String.valueOf(yearNum);


}

public static void getPrayersJsonArray(String url, RequestQueue queue){

    final JSONArray[] prayerJsonArray = new JSONArray[1];

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
            , url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            try {

                 prayerJsonArray[0] = response.getJSONArray(PRAYER_JSON_ARRAY_KEY);
                 new PrayersAsynckTask().execute(prayerJsonArray[0]);

                //stringJsonArray = prayerJsonArray.toString();
                //Toast.makeText(PrayersHelper.mContext,stringJsonArray,Toast.LENGTH_LONG).show();
                //JSONObject object = (JSONObject) prayerJsonArray.get(0);
               // JSONObject jsonObject1 = object.getJSONObject(PRAYER_TIMES_KEY);
                //currentMonthTimesArray.add(jsonArray1.toString());
               // Toast.makeText(PrayersHelper.mContext,jsonObject1.getString(FAJR),Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });

queue.add(jsonObjectRequest);

//return  prayerJsonArray[0];
}


public static ArrayList<Prayers> getPrayersAsString(JSONArray jsonArray){

    ArrayList<Prayers> prayersArrayList = new ArrayList<>();
    try {
        for (int i=0;i<jsonArray.length();i++){
            JSONObject object = (JSONObject) jsonArray.get(i);
            JSONObject timingsObject = object.getJSONObject(PRAYER_TIMES_KEY);
            Prayers dayPrayers = new Prayers();
            dayPrayers.setFajrTime(timingsObject.getString(FAJR));
            dayPrayers.setSunRiseTime(timingsObject.getString(SUN_RISE));
            dayPrayers.setDuhrTime(timingsObject.getString(DUHR));
            dayPrayers.setAsrTime(timingsObject.getString(ASR));
            dayPrayers.setMaghribTime(timingsObject.getString(MAGHRIB));
            dayPrayers.setAishaTime(timingsObject.getString(AISHA));
            prayersArrayList.add(dayPrayers);
        }

    } catch (JSONException e) {
        e.printStackTrace();
    }

return prayersArrayList;
}


    public static ArrayList<Prayers> getPrayerObjectFromFireBase(DataSnapshot snapshot){
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
