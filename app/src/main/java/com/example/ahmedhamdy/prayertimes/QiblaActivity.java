package com.example.ahmedhamdy.prayertimes;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class QiblaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);

        Toolbar mToolbar =  findViewById(R.id.action_bar);
        mToolbar.setTitle(R.string.qibla_activty_title);
        setSupportActionBar(mToolbar);


        if (savedInstanceState == null){

            FragmentManager fragmentManager = getSupportFragmentManager();
            QiblaFragment qiblaFragment = new QiblaFragment();
            fragmentManager.beginTransaction().add(R.id.qibla_fragment_container,qiblaFragment).commit();


        }
    }
}
