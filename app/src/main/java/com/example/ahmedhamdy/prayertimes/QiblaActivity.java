package com.example.ahmedhamdy.prayertimes;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QiblaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);

        if (savedInstanceState == null){

            FragmentManager fragmentManager = getSupportFragmentManager();
            QiblaFragment qiblaFragment = new QiblaFragment();
            fragmentManager.beginTransaction().add(R.id.qibla_fragment_container,qiblaFragment).commit();


        }
    }
}
