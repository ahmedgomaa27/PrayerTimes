package com.example.ahmedhamdy.prayertimes;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class PrayersWidget extends AppWidgetProvider {


    public static ArrayList<Prayers> mPrayersList = new ArrayList<>();
    static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        final ValueEventListener mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                    mPrayersList = PrayersHelper.getPrayerObjectFromFireBase(dataSnapshot);
                // Construct the RemoteViews object
                Prayers mTodayPrayers = new Prayers();
                mTodayPrayers = mPrayersList.get(DateHelper.getCurrentDayAsInt() - 1);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.prayers_widget);

                views.setTextViewText(R.id.widget_fajr, mTodayPrayers.getFajrTime().substring(0, 5));
                // views.setTextViewText(R.id.widget_sunrise,mTodayPrayers.getSunRiseTime());
                views.setTextViewText(R.id.widget_dhur, mTodayPrayers.getDuhrTime().substring(0, 5));
                views.setTextViewText(R.id.widget_asr, mTodayPrayers.getAsrTime().substring(0, 5));
                views.setTextViewText(R.id.widget_maghrib, mTodayPrayers.getMaghribTime().substring(0, 5));
                views.setTextViewText(R.id.widget_isha, mTodayPrayers.getAishaTime().substring(0, 5));

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.getReference().addValueEventListener(mListener);
        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.prayers_widget);

        // Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

