package com.example.ahmedhamdy.prayertimes;

import org.parceler.Parcel;
import org.parceler.ParcelClass;

/**
 * Created by ahmed hamdy on 1/16/2018.
 */

@Parcel

public class Prayers {
    private  String fajrTime;
    private  String sunRiseTime;
    private  String duhrTime;
    private  String asrTime;
    private  String maghribTime;
    private  String aishaTime;

    public Prayers(){


    }

    public String getFajrTime() {
        return fajrTime;
    }

    public void setFajrTime(String fajrTime) {
        this.fajrTime = fajrTime;
    }

    public String getDuhrTime() {
        return duhrTime;
    }

    public void setDuhrTime(String duhrTime) {
        this.duhrTime = duhrTime;
    }

    public String getAsrTime() {
        return asrTime;
    }

    public void setAsrTime(String asrTime) {
        this.asrTime = asrTime;
    }

    public String getSunRiseTime() {
        return sunRiseTime;
    }

    public void setSunRiseTime(String sunRiseTime) {
        this.sunRiseTime = sunRiseTime;
    }

    public String getMaghribTime() {
        return maghribTime;
    }

    public void setMaghribTime(String maghribTime) {
        this.maghribTime = maghribTime;
    }

    public String getAishaTime() {
        return aishaTime;
    }

    public void setAishaTime(String aishaTime) {
        this.aishaTime = aishaTime;
    }
}
